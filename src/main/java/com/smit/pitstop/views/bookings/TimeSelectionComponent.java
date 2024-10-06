package com.smit.pitstop.views.bookings;

import com.smit.pitstop.service.PitstopService;
import com.smit.pitstop.service.model.Location;
import com.smit.pitstop.service.model.TimeSlot;
import com.smit.pitstop.views.NotificationHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TimeSelectionComponent extends VerticalLayout {
    private static final Logger logger = LoggerFactory.getLogger(TimeSelectionComponent.class);
    private final PitstopService service;
    private final NotificationHelper notificationHelper;
    private final Location chosenLocation;
    private final Select<TimeSlot> availableTimeSelect;
    private final Button bookTimeButton;
    private final List<TimeSlot> availableTimes;

    public TimeSelectionComponent(PitstopService service,
                                  NotificationHelper helper,
                                  Location location,
                                  List<TimeSlot> availableTimes) {
        this.service = service;
        this.notificationHelper = helper;
        this.chosenLocation = location;
        this.availableTimes = availableTimes;

        availableTimeSelect = createAvailableTimeSelect();
        bookTimeButton = createBookTimeButton();

        this.setWidthFull();
        this.setPadding(false);
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        this.add(availableTimeSelect, bookTimeButton);
        this.displayAvailableTimes();
    }

    private Select<TimeSlot> createAvailableTimeSelect() {
        Select<TimeSlot> availableTimes = new Select<>();
        availableTimes.setLabel("Select time:");
        availableTimes.addValueChangeListener(e -> this.bookTimeButton.setEnabled(true));
        availableTimes.setVisible(false);
        return availableTimes;
    }

    private Button createBookTimeButton() {
        Button bookTime = new Button("Book time!");
        bookTime.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bookTime.setPrefixComponent(VaadinIcon.CALENDAR_CLOCK.create());
        bookTime.addClickListener(e -> this.openBookTimeDialog(availableTimeSelect.getValue()));
        bookTime.setEnabled(false);
        bookTime.setVisible(false);
        return bookTime;
    }

    private void displayAvailableTimes() {
        this.availableTimeSelect.setItems(availableTimes);
        this.availableTimeSelect.setVisible(true);
        this.bookTimeButton.setVisible(true);
    }

    private void openBookTimeDialog(TimeSlot timeSlot) {
        logger.info("Chosen time: " + timeSlot.toString());
        BookTimeDialog dialog = new BookTimeDialog(timeSlot, chosenLocation, service, notificationHelper);
        dialog.open();
    }
}
