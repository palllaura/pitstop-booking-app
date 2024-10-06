package com.smit.pitstop.service;

import com.smit.pitstop.service.model.Location;

import java.util.List;

public interface IPitstopRepository {
    List<Location> getLocations();

    List<Location> getLocationsForCarService();

    List<Location> getLocationsForTruckService();
}
