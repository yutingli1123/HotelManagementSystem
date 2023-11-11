package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
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

    private final Employee employee = new Employee("Test", "test", "t@t.com", "test", null, 500);

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
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/employees/by-id/" + employeeId, Customer.class);
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
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/employees/by-email/" + employee.getEmail(), Customer.class);
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
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/employees/by-username/" + employee.getUsername(), Customer.class);
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
        ResponseEntity<Customer[]> response = client.getForEntity("/api/v1/employees", Customer[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Customer responseCustomer = responseBody[0];
        assertTrue(responseCustomer.getId() > 0, "Response body should have an ID.");
        assertEquals(employee.getName(), responseCustomer.getName());
        assertEquals(employee.getUsername(), responseCustomer.getUsername());
        assertEquals(employee.getEmail(), responseCustomer.getEmail());
        assertTrue(passwordEncoder.matches(employee.getPassword(), responseCustomer.getPassword()));
    }
}