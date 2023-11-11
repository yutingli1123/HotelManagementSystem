package ca.mcgill.ecse.hotelmanagementbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.mcgill.ecse.hotelmanagementbackend.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test for the customer service class.
 * 
 * @author Clara Jabbour
 */

@ExtendWith(MockitoExtension.class)
    public class TestCustomerService {

        @Mock
        private CustomerRepository customerRepository;

        @InjectMocks
        private CustomerService customerService;

        @BeforeEach
        public void setUp() {
        }

        @Test
        public void testFindByUsername() {
            // Define a sample customer
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setUsername("John");

            // Mock the behavior of customerRepository.findByUsername()
            when(customerRepository.findByUsername("John")).thenReturn(Optional.of(customer));

            // Define a username that doesn't exist in the repository
            String nonExistentUsername = "nonexistentUser";

            // Mock the behavior of customerRepository.findByUsername() when the username is not found
            when(customerRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

            // Verify that the service returns the expected customer when found
            Customer foundCustomer = customerService.findByUsername("John");
            assertNotNull(foundCustomer);
            assertEquals(customer.getId(), foundCustomer.getId());
            assertEquals(customer.getUsername(), foundCustomer.getUsername());

            // Verify that the service returns null when the username is not found
            Customer nonExistentCustomer = customerService.findByUsername(nonExistentUsername);
            assertNull(nonExistentCustomer);
        }

        @Test
        public void testFindById() {
            // Define a sample customer
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setUsername("John");

            // Mock the behavior of customerRepository.findById()
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

            // Define an ID that doesn't exist in the repository
            Long nonExistentId = 2L;

            // Mock the behavior of customerRepository.findById() when the ID is not found
            when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Verify that the service returns the expected customer when found
            Customer foundCustomer = customerService.findById(1L);
            assertNotNull(foundCustomer);
            assertEquals(customer.getId(), foundCustomer.getId());
            assertEquals(customer.getUsername(), foundCustomer.getUsername());

            // Verify that the service returns null when the ID is not found
            Customer nonExistentCustomer = customerService.findById(nonExistentId);
            assertNull(nonExistentCustomer);
        }


        @Test
        public void testFindByEmail() {
            // Define a sample customer
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setUsername("John");
            customer.setEmail("john@example.com");
        
            // Mock the behavior of customerRepository.findByEmail()
            when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
        
            // Define an email that doesn't exist in the repository
            String nonExistentEmail = "nonexistent@example.com";
        
            // Mock the behavior of customerRepository.findByEmail() when the email is not found
            when(customerRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());
        
            // Verify that the service returns the expected customer when found
            Customer foundCustomer = customerService.findByEmail("john@example.com");
            assertNotNull(foundCustomer);
            assertEquals(customer.getId(), foundCustomer.getId());
            assertEquals(customer.getUsername(), foundCustomer.getUsername());
            assertEquals(customer.getEmail(), foundCustomer.getEmail());
        
            // Verify that the service returns null when the email is not found
            Customer nonExistentCustomer = customerService.findByEmail(nonExistentEmail);
            assertNull(nonExistentCustomer);
        }
        
        @Test
        public void testSave() {
            // Define a sample customer to be saved
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setUsername("John");
            customer.setEmail("john@example.com");

            // Mock the behavior of customerRepository.save() to return the customer
            when(customerRepository.save(any(Customer.class))).thenReturn(customer);

            // Call the service to save the customer
            customerService.save(customer);

            // Verify that the service calls customerRepository.save with the correct argument
            verify(customerRepository, times(1)).save(customer);
        }

        
        
        @Test
        public void testDeleteById() {
            // Mock the behavior of customerRepository.deleteById()
            customerService.deleteById(1L);

            // Verify that the service calls deleteById with the correct argument
            verify(customerRepository, times(1)).deleteById(1L);
        }

        @Test
        public void testDeleteByUsername() {
            // Mock the behavior of customerRepository.deleteByUsername()
            customerService.deleteByUsername("John");

            // Verify that the service calls deleteByUsername with the correct argument
            verify(customerRepository, times(1)).deleteByUsername("John");
        }

        @Test
        public void testDeleteByEmail() {
            // Mock the behavior of customerRepository.deleteByEmail()
            customerService.deleteByEmail("john@example.com");

            // Verify that the service calls deleteByEmail with the correct argument
            verify(customerRepository, times(1)).deleteByEmail("john@example.com");
        }    
    }



