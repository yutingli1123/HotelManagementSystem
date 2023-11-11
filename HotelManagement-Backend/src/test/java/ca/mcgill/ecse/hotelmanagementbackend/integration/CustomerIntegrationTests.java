package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.repository.CustomerRepository;
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
public class CustomerIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private CustomerRepository customerRepository;

    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    private Long customerId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreate() {
        Customer customer = new Customer("Test", "test", "t@t.com", "test", null);

        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/customers", customer, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.customerId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/customers/by-id/" + customerId, Customer.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals("Test", response.getBody().getName());
        assertEquals("test", response.getBody().getUsername());
        assertEquals("t@t.com", response.getBody().getEmail());
        assertTrue(passwordEncoder.matches("test", response.getBody().getPassword()));
    }

    @Test
    @Order(3)
    public void testGetAll() {
        ResponseEntity<Customer[]> response = client.getForEntity("/api/v1/customers", Customer[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Customer customer = responseBody[0];
        assertTrue(customer.getId() > 0, "Response body should have an ID.");
        assertEquals("Test", customer.getName());
        assertEquals("test", customer.getUsername());
        assertEquals("t@t.com", customer.getEmail());
        assertTrue(passwordEncoder.matches("test", customer.getPassword()));
    }
}