package com.smit.pitstop.views.bookings;

import com.smit.pitstop.service.PitstopService;
import com.smit.pitstop.service.model.Location;
import com.smit.pitstop.service.model.TimeSlot;
import com.smit.pitstop.views.MainLayout;
import com.smit.pitstop.views.NotificationHelper;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Book service")
@Route(value = "bookings", layout = MainLayout.class)
public class BookServiceView extends VerticalLayout {
    private final PitstopService service;
    private final NotificationHelper notificationHelper;
    private final FilterLayout filterLayout;
    private final VerticalLayout timeSelectionLayout;
    private TimeSelectionComponent timeSelectionComponent;

    public BookServiceView(PitstopService pitstopService) {
        this.notificationHelper = new NotificationHelper();
        this.service = pitstopService;

        this.filterLayout = new FilterLayout(service, notificationHelper, this);
        this.timeSelectionLayout = new VerticalLayout();

        this.add(filterLayout, timeSelectionLayout);
    }

    public void removeTimeSelectionComponent() {
        if (timeSelectionComponent != null) {
            timeSelectionLayout.remove(timeSelectionComponent);
            timeSelectionComponent = null;
        }
    }

    public void createTimeSelectionComponent(List<TimeSlot> availableTimes, Location chosenLocation) {
        if (timeSelectionComponent != null) {
            return;
        }
        timeSelectionComponent = new TimeSelectionComponent(
                service, notificationHelper, chosenLocation, availableTimes);
        timeSelectionLayout.add(timeSelectionComponent);
    }
}
