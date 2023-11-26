package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// Start the app for real.
// Use a random port to avoid conflicts.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Use the same instance for all tests. By default, JUnit creates a new instance per method.
@TestInstance(Lifecycle.PER_CLASS)
// Run the tests in the order specified by the @Order annotations (e.g., create before read).
@TestMethodOrder(OrderAnnotation.class)
public class ReservationIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private ReservationRepository reservationRepository;

    private final Reservation reservation = new Reservation(new Date(),new Date(),new Room());

    private Long reservationId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        reservationRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreate() {


        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/reservations", reservation, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.reservationId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<Reservation> response = client.getForEntity("/api/v1/reservations/by-id/{id}", Reservation.class, reservationId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(reservation.getCheckInDate(), response.getBody().getCheckInDate());
        assertEquals(reservation.getCheckOutDate(),response.getBody().getCheckOutDate());
        assertEquals(reservation.getCustomer(), response.getBody().getCustomer());
        assertEquals(reservation.getRoom(), response.getBody().getRoom());
    }

    @Test
    @Order(3)
    public void testGetByDateBetween() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservation.getCheckInDate());
        calendar.add(Calendar.DATE, -1);
        Date startDate = calendar.getTime();
        calendar.setTime(reservation.getCheckOutDate());
        calendar.add(Calendar.DATE, 2);
        Date endDate = calendar.getTime();
        ResponseEntity<Reservation[]> response = client.getForEntity("/api/v1/reservations/by-date-range?startDate={startDate}&endDate={endDate}", Reservation[].class, startDate, endDate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Reservation[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Reservation responseReservation = responseBody[0];
        assertTrue(responseReservation.getId() > 0, "Response body should have an ID.");
        assertEquals(reservation.getCheckInDate(), responseReservation.getCheckInDate());
        assertEquals(reservation.getCheckOutDate(),responseReservation.getCheckOutDate());

        assertEquals(reservation.getCustomer(), responseReservation.getCustomer());
        assertEquals(reservation.getRoom(), responseReservation.getRoom());
    }

    @Test
    @Order(4)
    public void testGetAll() {
        ResponseEntity<Reservation[]> response = client.getForEntity("/api/v1/reservations", Reservation[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Reservation[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Reservation responseReservation = responseBody[0];
        assertTrue(responseReservation.getId() > 0, "Response body should have an ID.");
        assertEquals(reservation.getCheckInDate(), responseReservation.getCheckInDate());
        assertEquals(reservation.getCheckOutDate(),responseReservation.getCheckOutDate());
        assertEquals(reservation.getCustomer(), responseReservation.getCustomer());
        assertEquals(reservation.getRoom(), responseReservation.getRoom());
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        Reservation reservation = new Reservation(new Date(),new Date(),new Room());
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/reservations", reservation, Long.class);
        // Save the ID to read later
        Long reservationId = responseCreate.getBody();
        client.delete("/api/v1/reservations/by-id/{id}", reservationId);
        ResponseEntity<Reservation> responseGet = client.getForEntity("/api/v1/reservations/by-id/{id}", Reservation.class, reservationId);
        assertNull(responseGet.getBody());
    }


}