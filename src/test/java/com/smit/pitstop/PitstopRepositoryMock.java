package com.smit.pitstop;

import com.smit.pitstop.service.IPitstopRepository;
import com.smit.pitstop.service.model.Location;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Getter
@Repository
public class PitstopRepositoryMock implements IPitstopRepository {

    private final List<Location> locations;

    public PitstopRepositoryMock() {
        locations = new ArrayList<>();
        locations.add(new Location(
                0,
                "Test 1",
                "",
                1,
                "Address 1",
                true,
                false
        ));
        locations.add(new Location(
                1,
                "Test 2",
                "",
                2,
                "Address 2",
                true,
                true
        ));
    }

    public List<Location> getLocationsForCarService() {
        return locations.stream().filter(Location::getSupportsCars).toList();
    }

    public List<Location> getLocationsForTruckService() {
        return locations.stream().filter(Location::getSupportsTrucks).toList();
    }
}
