package com.smit.pitstop.service;

import com.smit.pitstop.MockApiConnector;
import com.smit.pitstop.PitstopRepositoryMock;
import com.smit.pitstop.service.connector.IApiConnector;
import com.smit.pitstop.service.model.Location;
import com.smit.pitstop.service.model.TimeSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class PitstopServiceTest {

    @Autowired
    private PitstopService service;

    @Test
    void findAllLocations() {
        List<Location> locations = service.findAllLocations();
        Assertions.assertEquals(locations.size(), 2);
    }

    @Test
    void findAllLocationsForCarService() {
        List<Location> locations = service.findAllLocationsForCarService();
        Assertions.assertEquals(locations.size(), 2);
    }

    @Test
    void findAllLocationsForTruckService() {
        List<Location> locations = service.findAllLocationsForTruckService();
        Assertions.assertEquals(locations.size(), 1);
    }

    @Test
    void sendBookingRequestWithValidTime() {
        Location location = service.findAllLocations().get(0);
        TimeSlot timeSlot = new TimeSlot("valid-time", LocalDateTime.now(), true);
        String contactInfo = "Do not call me. I will get anxious.";
        Assertions.assertTrue(service.sendBookingRequest(location, timeSlot, contactInfo));
    }

    @Test
    void sendBookingRequestWithInvalidTime() {
        Location location = service.findAllLocations().get(0);
        TimeSlot timeSlot = new TimeSlot("invalid-time", LocalDateTime.now(), true);
        String contactInfo = "Do not call me. I will get anxious.";
        Assertions.assertFalse(service.sendBookingRequest(location, timeSlot, contactInfo));
    }

    @Test
    void getAvailableTimes() {
        Location location = service.findAllLocations().get(0);
        LocalDate from = LocalDate.parse("2024-10-08");
        LocalDate to = LocalDate.parse("2024-10-09");

        List<TimeSlot> result = service.getAvailableTimes(location, from, to);

        Assertions.assertTrue(result.stream().noneMatch(slot -> slot.getTime().toLocalDate().isAfter(to)));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public IPitstopRepository pitstopRepository() {
            return new PitstopRepositoryMock();
        }

        @Bean
        @Primary
        public IApiConnector xmlApiConnector() {
            return new MockApiConnector();
        }

        @Bean
        @Primary
        public IApiConnector jsonApiConnector() {
            return new MockApiConnector();
        }
    }
}
