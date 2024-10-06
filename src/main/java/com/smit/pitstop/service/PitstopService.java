package com.smit.pitstop.service;

import com.smit.pitstop.service.connector.IApiConnector;
import com.smit.pitstop.service.connector.XmlApiConnector;
import com.smit.pitstop.service.model.Location;
import com.smit.pitstop.service.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PitstopService {
    private static final Logger logger = LoggerFactory.getLogger(PitstopService.class);
    private final IPitstopRepository repository;
    private final IApiConnector xmlApiConnector;
    private final IApiConnector jsonApiConnector;

    @Autowired
    public PitstopService(
            @Qualifier("pitstopRepository") IPitstopRepository repository,
            @Qualifier("xmlApiConnector") IApiConnector xmlApiConnector,
            @Qualifier("jsonApiConnector") IApiConnector jsonApiConnector
    ) {
        this.repository = repository;
        this.xmlApiConnector = xmlApiConnector;
        this.jsonApiConnector = jsonApiConnector;
    }

    public List<Location> findAllLocations() {
        return repository.getLocations();
    }

    public List<Location> findAllLocationsForCarService() {
        return repository.getLocationsForCarService();
    }

    public List<Location> findAllLocationsForTruckService() {
        return repository.getLocationsForTruckService();
    }

    public boolean sendBookingRequest(Location chosenLocation, TimeSlot chosenTimeSlot, String info) {
        String uuid = chosenTimeSlot.getUuid();
        String apiAddress = chosenLocation.getApiAddress();
        Integer apiVersion = chosenLocation.getApiVersion();

        IApiConnector connector = getConnectorByVersion(apiVersion);
        if (connector == null) {
            return false;
        }
        return connector.sendTireChangeBookingRequest(apiAddress, uuid, info);
    }

    public List<TimeSlot> getAvailableTimes(Location location, LocalDate from, LocalDate to) {
        int apiVersion = location.getApiVersion();
        String apiAddress = location.getApiAddress();

        IApiConnector connector = getConnectorByVersion(apiVersion);
        if (connector == null) {
            return null;
        }
        List<TimeSlot> availableTimes = connector.getAvailableTimes(apiAddress, from, to);
        if (availableTimes == null) {
            return null;
        }
        return removeUnnecessaryTimeSlots(availableTimes, to);
    }

    private IApiConnector getConnectorByVersion(Integer apiVersion) {
        return switch (apiVersion) {
            case 1 -> xmlApiConnector;
            case 2 -> jsonApiConnector;
            default -> null;
        };
    }

    private List<TimeSlot> removeUnnecessaryTimeSlots(List<TimeSlot> listToEdit, LocalDate toDate) {
        LocalDateTime now = LocalDateTime.now();
        listToEdit.removeIf(time -> time.getTime().isBefore(now) || time.getTime().toLocalDate().isAfter(toDate));
        return listToEdit;
    }
}
