package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class EmployeeTest {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TimeTableService timeTableService;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void testSaveEmployee() {
        List<Employee> originalEmployees = employeeService.findAll();
        Employee employee1 = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", 123);
        Employee employee2 = new Employee("Emp2", "emp2", "emp2@test.com", "emp2", 666);
        Employee employee3 = new Employee("Emp3", "emp3", "emp3@test.com", "emp3", 888);
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
    }

    @Test
    @Transactional
    void testSearchEmployeeByEmail() {
        List<Employee> originalEmployees = employeeService.findAll();
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", 999);
        employeeService.save(employee);
        Employee employeeByEmail = employeeService.findByEmail("emp1@test.com");
        assertEquals("checks if employee searched by email matches the employee saved", employee, employeeByEmail);
        employeeService.deleteByEmail("emp1@test.com");
        assertEquals("checks if employee can be deleted by email", originalEmployees.size(), employeeService.findAll().size());
    }

    @Test
    @Transactional
    void testSearchEmployeeById() {
        List<Employee> originalEmployees = employeeService.findAll();
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", 999);
        employeeService.save(employee);
        Long employeeId = employee.getId();
        Employee employeeById = employeeService.findById(employeeId);
        assertEquals("checks if employee searched by id matches the employee saved", employee, employeeById);
        employeeService.deleteById(employeeId);
        assertEquals("checks if employee can be deleted by id", originalEmployees.size(), employeeService.findAll().size());
    }

    @Test
    @Transactional
    void testSearchEmployeeByUserName() {
        List<Employee> originalEmployees = employeeService.findAll();
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", 999);
        employeeService.save(employee);
        Employee employeeByUserName = employeeService.findByUsername("emp1");
        assertEquals("checks if employee searched by userName matches the employee saved", employee, employeeByUserName);
        employeeService.deleteByUsername("emp1");
        assertEquals("checks if employee can be deleted by userName", originalEmployees.size(), employeeService.findAll().size());
    }

    @Test
    @Transactional
    void testEmployeeReadAndWrite() {
        Employee employee = new Employee("Emp1", "emp1", "emp1@test.com", "emp1", 999);
        employeeService.save(employee);
        Integer salary = employee.getSalary();
        assertEquals("checks if employee salary can be correctly read", 999, salary);
        employee.setSalary(9999);
        salary = employee.getSalary();
        assertEquals("checks if employee salary can be correctly written", 9999, salary);
        employeeService.deleteByUsername("emp1");
    }

    @Test
    @Transactional
    void testEmployeeReferenceWithHotel() {
        Employee employee = new Employee("Test1", "test1", "test1@test.com", "test1", 668);
        employeeService.save(employee);
        //write
        TimeTable timeTable = new TimeTable();
        timeTableService.save(timeTable);
        employee.setTimeTable(timeTable);
        employeeService.save(employee);
        assertEquals("checks if employee can write its attribute which is an instance of TimeTable class", timeTable, employee.getTimeTable());
    }
}
