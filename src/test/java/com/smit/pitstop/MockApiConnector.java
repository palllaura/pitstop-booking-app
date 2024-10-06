package com.smit.pitstop;

import com.smit.pitstop.service.connector.IApiConnector;
import com.smit.pitstop.service.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MockApiConnector implements IApiConnector {

    public List<TimeSlot> getAvailableTimes(String apiAddress, LocalDate fromDate, LocalDate toDate) {
        List<TimeSlot> times = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        times.add(new TimeSlot("1", LocalDateTime.parse("2024-10-08 12:00", formatter), true));
        times.add(new TimeSlot("2", LocalDateTime.parse("2024-10-09 12:00", formatter), true));
        times.add(new TimeSlot("3", LocalDateTime.parse("2024-10-10 12:00", formatter), true));

        return times;
    }

    public boolean sendTireChangeBookingRequest(String apiAddress, String uuid, String contactInfo) {
        return uuid.equals("valid-time");
    }
}
