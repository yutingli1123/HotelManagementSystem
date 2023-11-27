package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import ca.mcgill.ecse.hotelmanagementbackend.repository.RoomRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

// Start the app for real.
// Use a random port to avoid conflicts.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Use the same instance for all tests. By default, JUnit creates a new instance per method.
@TestInstance(Lifecycle.PER_CLASS)
// Run the tests in the order specified by the @Order annotations (e.g., create before read).
@TestMethodOrder(OrderAnnotation.class)
public class RoomIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private RoomRepository roomRepository;

    private final Room room = new Room(RoomType.REGULAR, 500);

    private Long roomId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        roomRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreate() {
        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/rooms", room, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.roomId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<Room> response = client.getForEntity("/api/v1/rooms/by-id/{id}", Room.class, roomId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(room.getType(), response.getBody().getType());
        assertEquals(room.getFee(), response.getBody().getFee());
        assertEquals(room.getReservations(), response.getBody().getReservations());
        
    }

    @Test
    @Order(3)
    public void testGetAll() {
        ResponseEntity<Room[]> response = client.getForEntity("/api/v1/rooms", Room[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Room[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Room responseRoom = responseBody[0];
        assertTrue(responseRoom.getId() > 0, "Response body should have an ID.");
        assertEquals(room.getType(), responseRoom.getType());
        assertEquals(room.getFee(), responseRoom.getFee());
        assertEquals(room.getReservations(), responseRoom.getReservations());
        
    }

    @Test
    @Order(4)
    public void testGetAllByType() {
        ResponseEntity<Room[]> response = client.getForEntity("/api/v1/rooms/by-type/{type}", Room[].class, room.getType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Room[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Room responseRoom = responseBody[0];
        assertTrue(responseRoom.getId() > 0, "Response body should have an ID.");
        assertEquals(room.getType(), responseRoom.getType());
        assertEquals(room.getFee(), responseRoom.getFee());
        assertEquals(room.getReservations(), responseRoom.getReservations());
        
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        Room room = new Room(RoomType.REGULAR, 500);
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/rooms", room, Long.class);
        // Save the ID to read later
        Long roomId = responseCreate.getBody();
        client.delete("/api/v1/rooms/by-id/{id}", roomId);
        ResponseEntity<Owner> responseGet = client.getForEntity("/api/v1/rooms/by-id/{id}", Owner.class, roomId);
        assertNull(responseGet.getBody());
    }
}