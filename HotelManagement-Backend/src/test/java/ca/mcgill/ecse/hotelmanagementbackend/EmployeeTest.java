package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class EmployeeTest {
    @Autowired
    private EmployeeService employeeService;
    private HotelService hotelService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveEmployee(){
        List<Employee> originalEmployees = employeeService.findAll();
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Employee employee1 = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", hotel, 123);
        Employee employee2 = new Employee("Emp2", "emp2", "emp2@test.com", "emp2", hotel, 666);
        Employee employee3 = new Employee("Emp3", "emp3", "emp3@test.com", "emp3", hotel, 888);
        employeeService.save(employee1);
        employeeService.save(employee2);
        employeeService.save(employee3);
        assertEquals("checks if db has three employees saved", originalEmployees.size() + 3, employeeService.findAll().size());
        employeeService.deleteByUsername("emp1");
        assertEquals("checks if db has two employees saved", originalEmployees.size() + 2, employeeService.findAll().size());
        employeeService.deleteByUsername("emp2");
        assertEquals("checks if db has one employee saved", originalEmployees.size() + 1, employeeService.findAll().size());
        employeeService.deleteByUsername("emp3");
        assertEquals("checks if db has the original employees saved", originalEmployees.size(), employeeService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    @Test
    void testSearchEmployeeByEmail(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Employee> originalEmployees = employeeService.findAll();
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", hotel, 999);
        employeeService.save(employee);
        Employee employeeByEmail = employeeService.findByEmail("emp1@test.com");
        assertEquals("checks if employee searched by email matches the employee saved", employee, employeeByEmail);
        employeeService.deleteByEmail("emp1@test.com");
        assertEquals("checks if employee can be deleted by email", originalEmployees.size(), employeeService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    @Test
    void testSearchEmployeeById(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Employee> originalEmployees = employeeService.findAll();
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", hotel, 999);
        employeeService.save(employee);
        Long employeeId = employee.getId();
        Employee employeeById = employeeService.findById(employeeId);
        assertEquals("checks if employee searched by id matches the employee saved", employee, employeeById);
        employeeService.deleteById(employeeId);
        assertEquals("checks if employee can be deleted by id", originalEmployees.size(), employeeService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    void testSearchEmployeeByUserName(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Employee> originalEmployees = employeeService.findAll();
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", hotel, 999);
        employeeService.save(employee);
        Employee employeeByUserName = employeeService.findByUsername("emp1");
        assertEquals("checks if employee searched by userName matches the employee saved", employee, employeeByUserName);
        employeeService.deleteByUsername("emp1");
        assertEquals("checks if employee can be deleted by userName", originalEmployees.size(), employeeService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    void testEmployeeReadAndWrite() {
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", hotel, 999);
        employeeService.save(employee);
        Integer salary = employee.getSalary();
        assertEquals("checks if employee salary can be correctly read", 999 , salary);
        employee.setSalary(9999);
        salary = employee.getSalary();
        assertEquals("checks if employee salary can be correctly written", 9999, salary);
        employeeService.deleteByUsername("emp1");
        hotelService.deleteById(hotel.getId());
    }

    void testEmployeeReferenceWithHotel(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Employee employee = new Employee("Test1", "test1", "test1@test.com", "test1", hotel, 668);
        employeeService.save(employee);
        Hotel hotelByEmployee =  employee.getHotel();
        assertEquals("checks if the employee class can successfully find the hotel class", hotel, hotelByEmployee);
    }
}
