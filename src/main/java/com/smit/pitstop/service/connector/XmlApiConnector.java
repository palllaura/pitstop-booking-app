package com.smit.pitstop.service.connector;

import com.smit.pitstop.service.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class XmlApiConnector implements IApiConnector {
    private static final Logger logger = LoggerFactory.getLogger(XmlApiConnector.class);
    private final HttpClient client;

    public XmlApiConnector() {
        this.client = HttpClient.newHttpClient();
    }

    private static HttpURLConnection getHttpURLConnection(String contactInfo, String apiUrl) throws IOException {
        String xmlRequestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<london.tireChangeBookingRequest>\n"
                + "    <contactInformation>" + contactInfo + "</contactInformation>\n"
                + "</london.tireChangeBookingRequest>";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestProperty("Accept", "text/xml");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = xmlRequestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }

    public List<TimeSlot> getAvailableTimes(String apiAddress, LocalDate fromDate, LocalDate toDate) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiAddress + "/tire-change-times/available?from="
                        + fromDate.toString() + "&until=" + toDate.plusDays(1)))
                .header("Accept", "text/xml")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String xmlResponse = response.body();
                return parseXmlToList(xmlResponse);
            } else {
                logger.error("API call failed with status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Exception occurred while making API call: ", e);
        }
        return null;
    }

    public boolean sendTireChangeBookingRequest(String apiAddress, String uuid, String contactInfo) {
        try {
            String apiUrl = apiAddress + "/tire-change-times/" + uuid + "/booking";
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
            logger.error("Exception occurred while sending request: ", e);
            return false;
        }
    }

    private List<TimeSlot> parseXmlToList(String xmlResponse) {
        List<TimeSlot> availableTimes = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("availableTime");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String uuid = element.getElementsByTagName("uuid").item(0).getTextContent();

                    String timeString = element.getElementsByTagName("time").item(0).getTextContent();
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(timeString);
                    LocalDateTime time = offsetDateTime.toLocalDateTime();

                    availableTimes.add(new TimeSlot(uuid, time, true));
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred while parsing received data: ", e);
        }
        return availableTimes;
    }
}
