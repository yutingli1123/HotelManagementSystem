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

    private final Customer customer = new Customer("Test", "test", "t@t.com", "test");

    private Long customerId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreate() {


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
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/customers/by-id/{id}", Customer.class, customerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(customer.getName(), response.getBody().getName());
        assertEquals(customer.getUsername(), response.getBody().getUsername());
        assertEquals(customer.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(customer.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(3)
    public void testGetByEmail() {
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/customers/by-email/{email}", Customer.class, customer.getEmail());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(customer.getName(), response.getBody().getName());
        assertEquals(customer.getUsername(), response.getBody().getUsername());
        assertEquals(customer.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(customer.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(4)
    public void testGetByUsername() {
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/customers/by-username/{username}", Customer.class, customer.getUsername());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(customer.getName(), response.getBody().getName());
        assertEquals(customer.getUsername(), response.getBody().getUsername());
        assertEquals(customer.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(customer.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(5)
    public void testGetAll() {
        ResponseEntity<Customer[]> response = client.getForEntity("/api/v1/customers", Customer[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Customer responseCustomer = responseBody[0];
        assertTrue(responseCustomer.getId() > 0, "Response body should have an ID.");
        assertEquals(customer.getName(), responseCustomer.getName());
        assertEquals(customer.getUsername(), responseCustomer.getUsername());
        assertEquals(customer.getEmail(), responseCustomer.getEmail());
        assertTrue(passwordEncoder.matches(customer.getPassword(), responseCustomer.getPassword()));
    }

    @Test
    @Order(6)
    public void testGetAllByName() {
        ResponseEntity<Customer[]> response = client.getForEntity("/api/v1/customers/by-name/{name}", Customer[].class, customer.getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Customer responseCustomer = responseBody[0];
        assertTrue(responseCustomer.getId() > 0, "Response body should have an ID.");
        assertEquals(customer.getName(), responseCustomer.getName());
        assertEquals(customer.getUsername(), responseCustomer.getUsername());
        assertEquals(customer.getEmail(), responseCustomer.getEmail());
        assertTrue(passwordEncoder.matches(customer.getPassword(), responseCustomer.getPassword()));
    }

    @Test
    @Order(7)
    public void testDeleteById() {
        Customer customer = new Customer("Test2", "test2", "t2@t.com", "test2");
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/customers", customer, Long.class);
        // Save the ID to read later
        Long customerId = responseCreate.getBody();
        client.delete("/api/v1/customers/by-id/{id}", customerId);
        ResponseEntity<Customer> responseGet = client.getForEntity("/api/v1/customers/by-id/{id}", Customer.class, customerId);
        assertNull(responseGet.getBody());
    }

    @Test
    @Order(8)
    public void testDeleteByEmail() {
        Customer customer = new Customer("Test2", "test2", "t2@t.com", "test2");
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/customers", customer, Long.class);
        // Save the ID to read later
        Long customerId = responseCreate.getBody();
        client.delete("/api/v1/customers/by-email/{email}", customer.getEmail());
        ResponseEntity<Customer> responseGet = client.getForEntity("/api/v1/customers/by-id/{id}", Customer.class, customerId);
        assertNull(responseGet.getBody());
    }

    @Test
    @Order(9)
    public void testDeleteByUsername() {
        Customer customer = new Customer("Test2", "test2", "t2@t.com", "test2");
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/customers", customer, Long.class);
        // Save the ID to read later
        Long customerId = responseCreate.getBody();
        client.delete("/api/v1/customers/by-username/{username}", customer.getUsername());
        ResponseEntity<Customer> responseGet = client.getForEntity("/api/v1/customers/by-id/{id}", Customer.class, customerId);
        assertNull(responseGet.getBody());
    }
}