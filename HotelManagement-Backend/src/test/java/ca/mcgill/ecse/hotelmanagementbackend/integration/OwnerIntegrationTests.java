package ca.mcgill.ecse.hotelmanagementbackend.integration;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.repository.OwnerRepository;
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
public class OwnerIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private OwnerRepository ownerRepository;

    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    private final Owner owner = new Owner("Test", "test", "t@t.com", "test", null);

    private Long ownerId;

    // NOT @AfterEach, otherwise the person created in the POST test will be deleted before the GET test
    @AfterAll
    public void clearDatabase() {
        ownerRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreate() {


        // Send the request
        ResponseEntity<Long> response = client.postForEntity("/api/v1/owners", owner, Long.class);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Save the ID to read later
        this.ownerId = response.getBody();
    }

    @Test
    @Order(2)
    public void testGetById() {
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/owners/by-id/{id}", Customer.class, ownerId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(owner.getName(), response.getBody().getName());
        assertEquals(owner.getUsername(), response.getBody().getUsername());
        assertEquals(owner.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(owner.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(3)
    public void testGetByEmail() {
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/owners/by-email/{email}", Customer.class, owner.getEmail());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(owner.getName(), response.getBody().getName());
        assertEquals(owner.getUsername(), response.getBody().getUsername());
        assertEquals(owner.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(owner.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(4)
    public void testGetByUsername() {
        ResponseEntity<Customer> response = client.getForEntity("/api/v1/owners/by-username/{username}", Customer.class, owner.getUsername());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "Response body should have an ID.");
        assertEquals(owner.getName(), response.getBody().getName());
        assertEquals(owner.getUsername(), response.getBody().getUsername());
        assertEquals(owner.getEmail(), response.getBody().getEmail());
        assertTrue(passwordEncoder.matches(owner.getPassword(), response.getBody().getPassword()));
    }

    @Test
    @Order(5)
    public void testGetAll() {
        ResponseEntity<Customer[]> response = client.getForEntity("/api/v1/owners", Customer[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Customer responseCustomer = responseBody[0];
        assertTrue(responseCustomer.getId() > 0, "Response body should have an ID.");
        assertEquals(owner.getName(), responseCustomer.getName());
        assertEquals(owner.getUsername(), responseCustomer.getUsername());
        assertEquals(owner.getEmail(), responseCustomer.getEmail());
        assertTrue(passwordEncoder.matches(owner.getPassword(), responseCustomer.getPassword()));
    }

    @Test
    @Order(6)
    public void testGetAllByName() {
        ResponseEntity<Customer[]> response = client.getForEntity("/api/v1/owners/by-name/{name}", Customer[].class, owner.getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Customer[] responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.length);
        assertNotNull(responseBody[0]);
        Customer responseCustomer = responseBody[0];
        assertTrue(responseCustomer.getId() > 0, "Response body should have an ID.");
        assertEquals(owner.getName(), responseCustomer.getName());
        assertEquals(owner.getUsername(), responseCustomer.getUsername());
        assertEquals(owner.getEmail(), responseCustomer.getEmail());
        assertTrue(passwordEncoder.matches(owner.getPassword(), responseCustomer.getPassword()));
    }

    @Test
    @Order(7)
    public void testDeleteById() {
        Owner owner = new Owner("Test2", "test2", "t2@t.com", "test2", null);
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/owners", owner, Long.class);
        // Save the ID to read later
        Long ownerId = responseCreate.getBody();
        client.delete("/api/v1/owners/by-id/{id}", ownerId);
        ResponseEntity<Owner> responseGet = client.getForEntity("/api/v1/owners/by-id/{id}", Owner.class, ownerId);
        assertNull(responseGet.getBody());
    }

    @Test
    @Order(8)
    public void testDeleteByEmail() {
        Owner owner = new Owner("Test2", "test2", "t2@t.com", "test2", null);
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/owners", owner, Long.class);
        // Save the ID to read later
        Long ownerId = responseCreate.getBody();
        client.delete("/api/v1/owners/by-email/{email}",owner.getEmail());
        ResponseEntity<Owner> responseGet = client.getForEntity("/api/v1/owners/by-id/{id}", Owner.class, ownerId);
        assertNull(responseGet.getBody());
    }

    @Test
    @Order(9)
    public void testDeleteByUsername() {
        Owner owner = new Owner("Test2", "test2", "t2@t.com", "test2", null);
        ResponseEntity<Long> responseCreate = client.postForEntity("/api/v1/owners", owner, Long.class);
        // Save the ID to read later
        Long ownerId = responseCreate.getBody();
        client.delete("/api/v1/owners/by-username/{username}", owner.getUsername());
        ResponseEntity<Owner> responseGet = client.getForEntity("/api/v1/owners/by-id/{id}", Owner.class, ownerId);
        assertNull(responseGet.getBody());
    }
}