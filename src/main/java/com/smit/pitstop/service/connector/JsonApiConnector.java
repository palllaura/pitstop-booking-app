package com.smit.pitstop.service.connector;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.smit.pitstop.service.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonApiConnector implements IApiConnector {
    private static final Logger logger = LoggerFactory.getLogger(JsonApiConnector.class);
    private final HttpClient client;

    public JsonApiConnector() {
        this.client = HttpClient.newHttpClient();
    }

    public List<TimeSlot> getAvailableTimes(String apiAddress, LocalDate fromDate, LocalDate toDate) {
        int numOfTimesPerDay = 9;
        int amountOfResults = (int) ChronoUnit.DAYS.between(fromDate, toDate) * numOfTimesPerDay;
        String url = apiAddress + "/tire-change-times?amount=" + amountOfResults + "&from=" + fromDate;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
                objectMapper.registerModule(module);

                TimeSlot[] timeSlots = objectMapper.readValue(response.body(), TimeSlot[].class);

                return Arrays.stream(timeSlots)
                        .filter(TimeSlot::isAvailable)
                        .collect(Collectors.toList());
            } else {
                logger.error("API call failed with status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Exception occurred while making API call: ", e);
        }
        return null;
    }

    public boolean sendTireChangeBookingRequest(String apiAddress, String id, String contactInfo) {
        try {
            String apiUrl = apiAddress + "/tire-change-times/" + id + "/booking";
            HttpURLConnection connection = getHttpURLConnection(contactInfo, apiUrl);

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                logger.info("Booking request successful, Response code: " + responseCode);
                return true;
            } else {
                logger.error("Booking request failed, Response code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            logger.error("Exception occurred while making API call: ", e);
            return false;
        }
    }

    private HttpURLConnection getHttpURLConnection(String contactInfo, String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = String.format(
                "{\"contactInformation\": \"%s\"}",
                contactInfo
        );
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
}
