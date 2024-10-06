package com.smit.pitstop.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "api_address")
    private String apiAddress;
    @Column(name = "api_version")
    private Integer apiVersion;
    @Column(name = "address")
    private String address;
    @Column(name = "supports_cars")
    private Boolean supportsCars;
    @Column(name = "supports_trucks")
    private Boolean supportsTrucks;

    public Location(Integer id, String name, String api_address, Integer api_version, String address,
                    Boolean supports_cars, Boolean supports_trucks) {
        this.id = id;
        this.name = name;
        this.apiAddress = api_address;
        this.apiVersion = api_version;
        this.address = address;
        this.supportsCars = supports_cars;
        this.supportsTrucks = supports_trucks;
    }

    public Location() {
    }

    @Override
    public String toString() {
        return name;
    }
}
