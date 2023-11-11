package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.repository.TaskRepository;
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
public class TaskIntergrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private TaskRepository taskRepository;


    private Long taskId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSave() {
        LocalTime startTime = LocalTime.of(8, 30);
        LocalTime endTime = LocalTime.of(10, 30);
        Task task = new Task(startTime, endTime, "Monday", "test", "a task to test");

        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/tasks", task, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.taskId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<Task> response = client.getForEntity("/api/v1/tasks/by-id/" + taskId, Task.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(LocalTime.of(8, 30), response.getBody().getStartTime());
        assertEquals(LocalTime.of(10, 30), response.getBody().getEndTime());
    }

    @Test
    @Order(3)
    public void testGetAllByStartDate() {
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/start", Task[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task task = responseBody[0];
        assertTrue(task.getId() > 0, "Response body should have an ID.");
        assertEquals(LocalTime.of(8, 30), task.getStartTime());
    }

    @Test
    @Order(4)
    public void testGetAllByEndDate() {
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/end", Task[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task task = responseBody[0];
        assertTrue(task.getId() > 0, "Response body should have an ID.");
        assertEquals(LocalTime.of(10, 30), task.getEndTime());
    }

    @Test
    @Order(5)
    public void testGetAllByStartDateRange() {
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/start", Task[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task task = responseBody[0];
        assertTrue(task.getId() > 0, "Response body should have an ID.");
        assertEquals(LocalTime.of(8, 30), task.getStartTime());
    }

    @Test
    @Order(6)
    public void testGetAllByEndDateRange() {
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/start", Task[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task task = responseBody[0];
        assertTrue(task.getId() > 0, "Response body should have an ID.");
        assertEquals(LocalTime.of(10, 30), task.getStartTime());
    }

    @Test
    @Order(7)
    public void testDeleteHotelById() {
        client.delete("/api/v1/tasks/by-id/" + taskId);
        ResponseEntity<Task> response = client.getForEntity("/api/v1/tasks/by-id/" + taskId, Task.class);
        assertNull(response.getBody());
    }
}