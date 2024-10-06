package com.smit.pitstop.views.bookings;

import com.smit.pitstop.service.PitstopService;
import com.smit.pitstop.service.model.Location;
import com.smit.pitstop.service.model.TimeSlot;
import com.smit.pitstop.service.model.VehicleType;
import com.smit.pitstop.views.NotificationHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FilterLayout extends VerticalLayout {
    private final PitstopService service;
    private final NotificationHelper notificationHelper;
    private static final Logger logger = LoggerFactory.getLogger(FilterLayout.class);
    private final BookServiceView parentView;
    private final List<VehicleType> vehicleTypes;
    private final List<Location> locations;
    private final Button findButton;
    private RadioButtonGroup<VehicleType> vehicleTypeRadio;
    private Select<Location> locationSelect;
    private DatePicker fromDate;
    private DatePicker toDate;


    public FilterLayout(PitstopService service, NotificationHelper helper, BookServiceView bookServiceView) {
        this.service = service;
        this.notificationHelper = helper;
        this.parentView = bookServiceView;

        vehicleTypes = new ArrayList<>(Arrays.asList(VehicleType.values()));
        locations = new ArrayList<>();

        vehicleTypeRadio = createVehicleTypesRadio();
        locationSelect = createLocationSelect();
        findButton = createFindButton();
        VerticalLayout dateRangePicker = createDateRangePicker();

        this.add(vehicleTypeRadio, locationSelect, dateRangePicker, findButton);

        this.setWidthFull();
        this.setPadding(false);
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private RadioButtonGroup<VehicleType> createVehicleTypesRadio() {
        vehicleTypeRadio = new RadioButtonGroup<>();
        vehicleTypeRadio.setLabel("Vehicle type:");

        vehicleTypeRadio.setItems(vehicleTypes);
        vehicleTypeRadio.addValueChangeListener(e -> this.selectVehicleType(vehicleTypeRadio.getValue()));
        vehicleTypeRadio.addValueChangeListener(e -> parentView.removeTimeSelectionComponent());

        vehicleTypeRadio.setRenderer(new ComponentRenderer<>(type -> {
            Icon icon = VaadinIcon.valueOf(type.getIconName()).create();
            icon.setTooltipText(type.getName());
            return new Div(icon);
        }));
        
        return vehicleTypeRadio;
    }

    private Select<Location> createLocationSelect() {
        locationSelect = new Select<>();
        locationSelect.setLabel("Select location:");
        locationSelect.addValueChangeListener(e -> this.enableDateRangeSelectors());
        locationSelect.addValueChangeListener(e -> parentView.removeTimeSelectionComponent());
        locationSelect.setEnabled(false);
        return locationSelect;
    }

    private VerticalLayout createDateRangePicker() {
        fromDate = createDatePicker("From:");
        toDate = createDatePicker("To:");

        toDate.setValue(LocalDate.now().plusDays(6));
        toDate.setMax(fromDate.getValue().plusDays(6));
        fromDate.addValueChangeListener(e -> toDate.setValue(fromDate.getValue().plusDays(6)));
        fromDate.addValueChangeListener(e -> toDate.setMin(fromDate.getValue()));
        fromDate.addValueChangeListener(e -> toDate.setMax(fromDate.getValue().plusDays(6)));

        VerticalLayout dateRangeLayout = new VerticalLayout(fromDate, toDate);
        dateRangeLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        dateRangeLayout.setPadding(false);

        return dateRangeLayout;
    }

    private DatePicker createDatePicker(String label) {
        DatePicker datePicker = new DatePicker(label);
        datePicker.setValue(LocalDate.now());
        datePicker.setMin(LocalDate.now());

        datePicker.addValueChangeListener(e -> findButton.setEnabled(validateFilters()));
        datePicker.addValueChangeListener(e -> parentView.removeTimeSelectionComponent());

        Locale systemLocale = Locale.getDefault();
        datePicker.setLocale(systemLocale);

        datePicker.setEnabled(false);

        return datePicker;
    }

    private Button createFindButton() {
        Button findButton = new Button("Find available times");
        findButton.setPrefixComponent(VaadinIcon.SEARCH.create());
        findButton.addClickListener(e -> this.doFind());
        findButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        findButton.setEnabled(false);
        return findButton;
    }

    private void enableDateRangeSelectors() {
        fromDate.setEnabled(true);
        toDate.setEnabled(true);
    }

    private boolean validateFilters() {
        return vehicleTypeRadio.getValue() != null
                && locationSelect.getValue() != null
                && fromDate.getValue() != null
                && toDate.getValue() != null;
    }

    private void selectVehicleType(VehicleType value) {
        locations.clear();
        if (value == VehicleType.CAR) {
            locations.addAll(service.findAllLocationsForCarService());
        } else if (value == VehicleType.TRUCK) {
            locations.addAll(service.findAllLocationsForTruckService());
        }
        locationSelect.setItems(locations);
        locationSelect.setPrefixComponent(VaadinIcon.MAP_MARKER.create());
        locationSelect.setEnabled(true);
        locationSelect.addValueChangeListener(e -> findButton.setEnabled(validateFilters()));
    }

    private void doFind() {
        Location chosenLocation = locationSelect.getValue();
        LocalDate from = fromDate.getValue();
        LocalDate until = toDate.getValue();

        if (chosenLocation == null || from == null || until == null) {
            notificationHelper.createNotification("Invalid selection", "Please select a location and valid dates.",
                    NotificationVariant.LUMO_ERROR);
            return;
        }
        logger.info("Finding available times between {} and {} in {}.", from, until, chosenLocation.getName());

        List<TimeSlot> availableTimes = this.service.getAvailableTimes(chosenLocation, from, until);

        if (availableTimes == null) {
            notificationHelper.createNotification("Failed to connect.", "Please try again later.",
                    NotificationVariant.LUMO_ERROR);
            return;
        }

        if (availableTimes.isEmpty()) {
            notificationHelper.createNotification("No available times.", "Please choose a different period.",
                    NotificationVariant.LUMO_WARNING);
            return;
        }
        logger.info("Found {} available times.", availableTimes.size());
        logger.info("Available times: {}", availableTimes);

        parentView.createTimeSelectionComponent(availableTimes, chosenLocation);
    }
}
