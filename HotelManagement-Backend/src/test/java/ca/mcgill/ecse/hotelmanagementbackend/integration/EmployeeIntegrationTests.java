package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

// Start the app for real.
// Use a random port to avoid conflicts.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Use the same instance for all tests. By default, JUnit creates a new instance per method.
@TestInstance(Lifecycle.PER_CLASS)
// Run the tests in the order specified by the @Order annotations (e.g., create before read).
@TestMethodOrder(OrderAnnotation.class)
public class EmployeeIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private EmployeeRepository employeeRepository;

    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    private final Employee employee = new Employee("Test", "test", "t@t.com", "test", 500);

    private Long employeeId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        employeeRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreate() {


        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/employees", employee, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.employeeId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<Employee> response = client.getForEntity("/api/v1/employees/by-id/{id}", Employee.class,employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(employee.getName(), response.getBody().getName());
        assertEquals(employee.getUsername(), response.getBody().getUsername());
        assertEquals(employee.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(employee.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(3)
    public void testGetByEmail() {
        ResponseEntity<Employee> response = client.getForEntity("/api/v1/employees/by-email/{email}", Employee.class, employee.getEmail());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(employee.getName(), response.getBody().getName());
        assertEquals(employee.getUsername(), response.getBody().getUsername());
        assertEquals(employee.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(employee.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(4)
    public void testGetByUsername() {
        ResponseEntity<Employee> response = client.getForEntity("/api/v1/employees/by-username/{username}", Employee.class, employee.getUsername());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(employee.getName(), response.getBody().getName());
        assertEquals(employee.getUsername(), response.getBody().getUsername());
        assertEquals(employee.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(employee.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(5)
    public void testGetAll() {
        ResponseEntity<Employee[]> response = client.getForEntity("/api/v1/employees", Employee[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Employee[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Employee responseEmployee = responseBody[0];
        assertTrue(responseEmployee.getId() > 0, "Response body should have an ID.");
        assertEquals(employee.getName(), responseEmployee.getName());
        assertEquals(employee.getUsername(), responseEmployee.getUsername());
        assertEquals(employee.getEmail(), responseEmployee.getEmail());
        assertTrue(passwordEncoder.matches(employee.getPassword(), responseEmployee.getPassword()));
    }

    @Test
    @Order(6)
    public void testGetAllByName() {
        ResponseEntity<Employee[]> response = client.getForEntity("/api/v1/employees/by-name/{name}", Employee[].class, employee.getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Employee[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Employee responseEmployee = responseBody[0];
        assertTrue(responseEmployee.getId() > 0, "Response body should have an ID.");
        assertEquals(employee.getName(), responseEmployee.getName());
        assertEquals(employee.getUsername(), responseEmployee.getUsername());
        assertEquals(employee.getEmail(), responseEmployee.getEmail());
        assertTrue(passwordEncoder.matches(employee.getPassword(), responseEmployee.getPassword()));
    }

    @Test
    @Order(7)
    public void testDeleteById() {
        Employee employee = new Employee("Test2", "test2", "t2@t.com", "test2", 500);
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/employees", employee, Long.class);
        // Save the ID to read later
        Long employeeId = responseCreate.getBody();
        client.delete("/api/v1/employees/by-id/{id}", employeeId);
        ResponseEntity<Employee> responseGet = client.getForEntity("/api/v1/employees/by-id/{id}", Employee.class, employeeId);

        assertNull(responseGet.getBody());
    }

    @Test
    @Order(8)
    public void testDeleteByEmail() {
        Employee employee = new Employee("Test2", "test2", "t2@t.com", "test2", 500);
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/employees", employee, Long.class);
        // Save the ID to read later
        Long employeeId = responseCreate.getBody();
        client.delete("/api/v1/employees/by-email/{email}", employee.getEmail());
        ResponseEntity<Employee> responseGet = client.getForEntity("/api/v1/employees/by-id/{id}", Employee.class, employeeId);
        assertNull(responseGet.getBody());
    }

    @Test
    @Order(9)
    public void testDeleteByUsername() {
        Employee employee = new Employee("Test2", "test2", "t2@t.com", "test2", 500);
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/employees", employee, Long.class);
        // Save the ID to read later
        Long employeeId = responseCreate.getBody();
        client.delete("/api/v1/employees/by-username/{username}", employee.getUsername());
        ResponseEntity<Employee> responseGet = client.getForEntity("/api/v1/employees/by-id/{id}", Employee.class, employeeId);
        assertNull(responseGet.getBody());
    }
}