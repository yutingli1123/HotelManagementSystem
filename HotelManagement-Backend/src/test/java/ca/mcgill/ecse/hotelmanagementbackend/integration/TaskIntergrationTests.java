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

    private final Task task = new Task(LocalTime.of(8, 30), LocalTime.of(10, 30), "Monday", "test", "a task to test");


    private Long taskId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSave() {
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
        ResponseEntity<Task> response = client.getForEntity("/api/v1/tasks/by-id/{id}", Task.class, taskId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(task.getStartTime(), response.getBody().getStartTime());
        assertEquals(task.getEndTime(), response.getBody().getEndTime());
        assertEquals(task.getTaskName(), response.getBody().getTaskName());
        assertEquals(task.getTaskDescription(), response.getBody().getTaskDescription());
        assertEquals(task.getDayOfTheWeek(), response.getBody().getDayOfTheWeek());
        assertEquals(task.getTimeTable(), response.getBody().getTimeTable());
    }

    @Test
    @Order(3)
    public void testGetAllByStartDate() {
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/start?startTime={startTime}", Task[].class, task.getStartTime());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task responseTask = responseBody[0];
        assertTrue(responseTask.getId() > 0, "Response body should have an ID.");
        assertEquals(task.getStartTime(), responseTask.getStartTime());
        assertEquals(task.getEndTime(), responseTask.getEndTime());
        assertEquals(task.getTaskName(), responseTask.getTaskName());
        assertEquals(task.getTaskDescription(), responseTask.getTaskDescription());
        assertEquals(task.getDayOfTheWeek(), responseTask.getDayOfTheWeek());
        assertEquals(task.getTimeTable(), responseTask.getTimeTable());
    }

    @Test
    @Order(4)
    public void testGetAllByEndDate() {
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/end?endTime={endTime}", Task[].class, task.getEndTime());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task responseTask = responseBody[0];
        assertTrue(responseTask.getId() > 0, "Response body should have an ID.");
        assertEquals(task.getStartTime(), responseTask.getStartTime());
        assertEquals(task.getEndTime(), responseTask.getEndTime());
        assertEquals(task.getTaskName(), responseTask.getTaskName());
        assertEquals(task.getTaskDescription(), responseTask.getTaskDescription());
        assertEquals(task.getDayOfTheWeek(), responseTask.getDayOfTheWeek());
        assertEquals(task.getTimeTable(), responseTask.getTimeTable());
    }

    @Test
    @Order(5)
    public void testGetAllByStartDateRange() {
        LocalTime startTime = task.getStartTime().minusHours(1);
        LocalTime endTime = task.getStartTime().plusHours(1);
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/start/range?startTime={startTime}&endTime={endTime}", Task[].class, startTime, endTime);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task responseTask = responseBody[0];
        assertTrue(responseTask.getId() > 0, "Response body should have an ID.");
        assertEquals(task.getStartTime(), responseTask.getStartTime());
        assertEquals(task.getEndTime(), responseTask.getEndTime());
        assertEquals(task.getTaskName(), responseTask.getTaskName());
        assertEquals(task.getTaskDescription(), responseTask.getTaskDescription());
        assertEquals(task.getDayOfTheWeek(), responseTask.getDayOfTheWeek());
        assertEquals(task.getTimeTable(), responseTask.getTimeTable());
    }

    @Test
    @Order(6)
    public void testGetAllByEndDateRange() {
        LocalTime startTime = task.getEndTime().minusHours(1);
        LocalTime endTime = task.getEndTime().plusHours(1);
        ResponseEntity<Task[]> response = client.getForEntity("/api/v1/tasks/by-time/end/range?startTime={startTime}&endTime={endTime}", Task[].class, startTime, endTime);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Task responseTask = responseBody[0];
        assertTrue(responseTask.getId() > 0, "Response body should have an ID.");
        assertEquals(task.getStartTime(), responseTask.getStartTime());
        assertEquals(task.getEndTime(), responseTask.getEndTime());
        assertEquals(task.getTaskName(), responseTask.getTaskName());
        assertEquals(task.getTaskDescription(), responseTask.getTaskDescription());
        assertEquals(task.getDayOfTheWeek(), responseTask.getDayOfTheWeek());
        assertEquals(task.getTimeTable(), responseTask.getTimeTable());
    }

    @Test
    @Order(7)
    public void testDeleteHotelById() {
        Task task = new Task(LocalTime.of(10, 30), LocalTime.of(12, 30), "Monday", "test2", "another task to test");
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/tasks", task, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

        // Save the ID to read later
        Long taskId = responseCreate.getBody();
        client.delete("/api/v1/tasks/by-id/{id}", taskId);
        ResponseEntity<Task> responseDelete = client.getForEntity("/api/v1/tasks/by-id/" + taskId, Task.class);
        assertNull(responseDelete.getBody());
    }
}