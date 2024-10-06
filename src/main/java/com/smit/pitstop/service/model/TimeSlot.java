package com.smit.pitstop.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class TimeSlot {
    @JsonProperty("id")
    private final String uuid;
    private final LocalDateTime time;
    private boolean available;

    public TimeSlot(@JsonProperty("id") String uuid,
                    @JsonProperty("time") LocalDateTime time,
                    @JsonProperty("available") Boolean isAvailable) {
        this.uuid = uuid;
        this.time = time;
        this.available = isAvailable;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return time.format(formatter);
    }
}
