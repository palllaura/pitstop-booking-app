package com.smit.pitstop.views.locations;

import com.smit.pitstop.service.PitstopService;
import com.smit.pitstop.service.model.Location;
import com.smit.pitstop.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Locations")
@Route(value = "locations", layout = MainLayout.class)
public class LocationsView extends VerticalLayout {
    private final PitstopService service;
    private final FormLayout viewLayout;

    public LocationsView(PitstopService service) {
        this.service = service;

        viewLayout = new FormLayout();
        viewLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("800px", 2));
        this.add(viewLayout);
        createLocations();
    }

    private void createLocations() {
        List<Location> locations = service.findAllLocations();

        for (Location location : locations) {
            viewLayout.add(createLocationLayout(location));
        }
    }

    private VerticalLayout createLocationLayout(Location location) {
        H1 header = new H1(location.getName());

        Icon mapIcon = VaadinIcon.MAP_MARKER.create();
        String address = location.getAddress();
        String formattedAddress = address.replace(" ", "+");
        String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + formattedAddress;
        Anchor googleMapsLink = new Anchor(googleMapsUrl, address);
        googleMapsLink.setTarget("_blank");
        HorizontalLayout addressLayout = new HorizontalLayout(mapIcon, googleMapsLink);

        Text info = new Text("Supported vehicle types:");
        HorizontalLayout icons = new HorizontalLayout();

        if (location.getSupportsCars()) {
            Icon carIcon = VaadinIcon.CAR.create();
            icons.add(carIcon);
        }

        if (location.getSupportsTrucks()) {
            Icon truckIcon = VaadinIcon.TRUCK.create();
            icons.add(truckIcon);
        }

        VerticalLayout vehicleTypesLayout = new VerticalLayout(info, icons);

        VerticalLayout locationLayout = new VerticalLayout();
        locationLayout.getStyle().set("background-color", "var(--lumo-shade-5pct)");
        locationLayout.add(header, addressLayout, vehicleTypesLayout);

        return locationLayout;
    }
}
