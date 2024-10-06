package com.smit.pitstop.service.connector;

import com.smit.pitstop.service.model.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface IApiConnector {
    List<TimeSlot> getAvailableTimes(String apiAddress, LocalDate fromDate, LocalDate toDate);

    boolean sendTireChangeBookingRequest(String apiAddress, String uuid, String contactInfo);
}
