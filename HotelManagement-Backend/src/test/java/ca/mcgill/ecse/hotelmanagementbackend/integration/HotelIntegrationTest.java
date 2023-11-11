package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.repository.HotelRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

// Start the app for real.
// Use a random port to avoid conflicts.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Use the same instance for all tests. By default, JUnit creates a new instance per method.
@TestInstance(Lifecycle.PER_CLASS)
// Run the tests in the order specified by the @Order annotations (e.g., create before read).
@TestMethodOrder(OrderAnnotation.class)
public class HotelIntegrationTest {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private HotelRepository hotelRepository;


    private Long hotelId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        hotelRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSave() {
        LocalTime startTime = LocalTime.of(8, 30);
        LocalTime closeTime = LocalTime.of(23, 30);
        Hotel hotel = new Hotel(startTime, closeTime);

        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/hotels", hotel, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.hotelId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<Hotel> response = client.getForEntity("/api/v1/hotels/" + hotelId, Hotel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(LocalTime.of(8, 30), response.getBody().getOpenTime());
        assertEquals(LocalTime.of(23, 30), response.getBody().getCloseTime());
    }

    @Test
    @Order(3)
    public void testGetAll() {
        ResponseEntity<Hotel[]> response = client.getForEntity("/api/v1/hotels", Hotel[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Hotel[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Hotel hotel = responseBody[0];
        assertTrue(hotel.getId() > 0, "Response body should have an ID.");
        assertEquals(LocalTime.of(8, 30), hotel.getOpenTime());
        assertEquals(LocalTime.of(23, 30), hotel.getCloseTime());
    }

    @Test
    @Order(4)
    public void testDeleteHotelById() {
        Hotel hotel = new Hotel(LocalTime.of(8, 30), LocalTime.of(12, 30));
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/hotels", hotel, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

        // Save the ID to read later
        Long hotelId = responseCreate.getBody();
        client.delete("/api/v1/hotels/" + hotelId);
        ResponseEntity<Hotel> responseDelete = client.getForEntity("/api/v1/hotels/" + hotelId, Hotel.class);
        assertNull(responseDelete.getBody());
    }
}