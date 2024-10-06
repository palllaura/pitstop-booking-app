package com.smit.pitstop.service;

import com.smit.pitstop.service.model.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitstopRepository extends CrudRepository<Location, Integer>, IPitstopRepository {

    @Query(value = "SELECT * FROM locations", nativeQuery = true)
    List<Location> getLocations();

    @Query(value = "SELECT * FROM locations l WHERE l.supports_cars = true ORDER BY l.name", nativeQuery = true)
    List<Location> getLocationsForCarService();

    @Query(value = "SELECT * FROM locations l WHERE l.supports_trucks = true ORDER BY l.name", nativeQuery = true)
    List<Location> getLocationsForTruckService();
}
