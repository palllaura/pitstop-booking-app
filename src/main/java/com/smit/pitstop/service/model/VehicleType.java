package com.smit.pitstop.service.model;

import lombok.Getter;

@Getter
public enum VehicleType {
    CAR("Car", "CAR"),
    TRUCK("Truck", "TRUCK");

    public final String name;
    public final String iconName;

    private VehicleType(String name, String iconName) {
        this.name = name;
        this.iconName = iconName;
    }
}
