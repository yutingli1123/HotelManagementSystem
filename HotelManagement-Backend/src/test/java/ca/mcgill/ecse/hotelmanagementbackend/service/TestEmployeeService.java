package ca.mcgill.ecse.hotelmanagementbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.mcgill.ecse.hotelmanagementbackend.repository.EmployeeRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for the employee service class.
 * 
 * @author Clara Jabbour
 */

@ExtendWith(MockitoExtension.class)
    public class TestEmployeeService {

        @Mock
        private EmployeeRepository employeeRepository;

        @InjectMocks
        private EmployeeService employeeService;

        @BeforeEach
        public void setup() {

        }

        @Test
        public void testFindByUsername() {
            // Define a sample employee
            Employee employee = new Employee();
            employee.setId(1L);
            employee.setUsername("John");

            // Mock the behavior of employeeRepository.findByUsername()
            when(employeeRepository.findByUsername("John")).thenReturn(Optional.of(employee));

            // Define a username that doesn't exist in the repository
            String nonExistentUsername = "NonExistentUser";

            // Mock the behavior of employeeRepository.findByUsername() when the username is not found
            when(employeeRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

            // Verify that the service returns the expected employee when found
            Employee foundEmployee = employeeService.findByUsername("John");
            assertNotNull(foundEmployee);
            assertEquals(employee.getId(), foundEmployee.getId());
            assertEquals(employee.getUsername(), foundEmployee.getUsername());

            // Verify that the service returns null when the username is not found
            Employee nonExistentEmployee = employeeService.findByUsername(nonExistentUsername);
            assertNull(nonExistentEmployee);
        }


        @Test
        public void testFindById() {
            // Define a sample employee
            Employee employee = new Employee();
            employee.setId(1L);
            employee.setUsername("John");

            // Mock the behavior of employeeRepository.findById()
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

            // Define an ID that doesn't exist in the repository
            Long nonExistentId = 2L;

            // Mock the behavior of employeeRepository.findById() when the ID is not found
            when(employeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Verify that the service returns the expected employee when found
            Employee foundEmployee = employeeService.findById(1L);
            assertNotNull(foundEmployee);
            assertEquals(employee.getId(), foundEmployee.getId());
            assertEquals(employee.getUsername(), foundEmployee.getUsername());

            // Verify that the service returns null when the ID is not found
            Employee nonExistentEmployee = employeeService.findById(nonExistentId);
            assertNull(nonExistentEmployee);
        }


        @Test
        public void testFindByEmail() {
            // Define a sample employee
            Employee employee = new Employee();
            employee.setId(1L);
            employee.setUsername("John");
            employee.setEmail("john@example.com");

            // Mock the behavior of employeeRepository.findByEmail()
            when(employeeRepository.findByEmail("john@example.com")).thenReturn(Optional.of(employee));

            // Define an email that doesn't exist in the repository
            String nonExistentEmail = "nonexistent@example.com";

            // Mock the behavior of employeeRepository.findByEmail() when the email is not found
            when(employeeRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

            // Verify that the service returns the expected employee when found
            Employee foundEmployee = employeeService.findByEmail("john@example.com");
            assertNotNull(foundEmployee);
            assertEquals(employee.getId(), foundEmployee.getId());
            assertEquals(employee.getUsername(), foundEmployee.getUsername());
            assertEquals(employee.getEmail(), foundEmployee.getEmail());

            // Verify that the service returns null when the email is not found
            Employee nonExistentEmployee = employeeService.findByEmail(nonExistentEmail);
            assertNull(nonExistentEmployee);
        }

        @Test
        public void testSave() {
            // Define a sample employee to be saved
            Employee employee = new Employee();
            employee.setId(1L);
            employee.setUsername("John");
            employee.setEmail("john@example.com");
        
            // Mock the behavior of employeeRepository.save() to return the employee
            when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        
            // Call the service to save the employee
            employeeService.save(employee);
        
            // Verify that the service calls employeeRepository.save with the correct argument
            verify(employeeRepository, times(1)).save(employee);
        }
        

        @Test
        public void testDeleteById() {
            // Mock the behavior of employeeRepository.deleteById()
            employeeService.deleteById(1L);

            // Verify that the service calls deleteById with the correct argument
            verify(employeeRepository, times(1)).deleteById(1L);
        }

        @Test
        public void testDeleteByUsername() {
            // Mock the behavior of employeeRepository.deleteByUsername()
            employeeService.deleteByUsername("John");
    
            // Verify that the service calls deleteByUsername with the correct argument
            verify(employeeRepository, times(1)).deleteByUsername("John");
        }

        @Test
        public void testDeleteByEmail() {
            // Mock the behavior of employeeRepository.deleteByEmail()
            employeeService.deleteByEmail("john@example.com");

            // Verify that the service calls deleteByEmail with the correct argument
            verify(employeeRepository, times(1)).deleteByEmail("john@example.com");
        }
    
    }
