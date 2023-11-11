package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.repository.TimeTableRepository;
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
public class TimeTableIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private TimeTableRepository timeTableRepository;

    private final TimeTable timeTable = new TimeTable("Test");

    private Long timeTableId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        timeTableRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreate() {
        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/timeTables", timeTable, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.timeTableId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<TimeTable> response = client.getForEntity("/api/v1/timeTables/by-id/{id}", TimeTable.class, timeTableId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(timeTable.getTimeTableName(), response.getBody().getTimeTableName());
        assertEquals(timeTable.getEmployees(), response.getBody().getEmployees());
        assertEquals(timeTable.getTasks(), response.getBody().getTasks());
    }

    @Test
    @Order(3)
    public void testGetAll() {
        ResponseEntity<TimeTable[]> response = client.getForEntity("/api/v1/timeTables", TimeTable[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TimeTable[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        TimeTable responseTimeTable = responseBody[0];
        assertTrue(responseTimeTable.getId() > 0, "Response body should have an ID.");
        assertEquals(timeTable.getTimeTableName(), responseTimeTable.getTimeTableName());
        assertEquals(timeTable.getEmployees(), responseTimeTable.getEmployees());
        assertEquals(timeTable.getTasks(), responseTimeTable.getTasks());
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        TimeTable timeTable = new TimeTable("Test2");
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/timeTables", timeTable, Long.class);
        // Save the ID to read later
        Long timeTableId = responseCreate.getBody();
        client.delete("/api/v1/timeTables/by-id/{id}", timeTableId);
        ResponseEntity<TimeTable> responseGet = client.getForEntity("/api/v1/timeTables/by-id/{id}", TimeTable.class, timeTableId);
        assertNull(responseGet.getBody());
    }
}