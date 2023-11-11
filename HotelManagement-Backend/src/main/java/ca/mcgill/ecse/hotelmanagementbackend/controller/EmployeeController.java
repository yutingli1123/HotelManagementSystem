package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16,32,1,60000,10);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @PostMapping
    public Long saveEmployee(@Valid @RequestBody Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeService.save(employee);
        return employee.getId();
    }

    @GetMapping("/by-username/{username}")
    public Employee getEmployeeByUsername(@PathVariable String username) {
        return employeeService.findByUsername(username);
    }

    @GetMapping("/by-email/{email}")
    public Employee getEmployeeByEmail(@PathVariable String email) {
        return employeeService.findByEmail(email);
    }

    @GetMapping("/by-id/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @DeleteMapping("/by-username/{username}")
    public void deleteEmployeeByUsername(@PathVariable String username) {
        employeeService.deleteByUsername(username);
    }

    @DeleteMapping("/by-email/{email}")
    public void deleteEmployeeByEmail(@PathVariable String email) {
        employeeService.deleteByEmail(email);
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteEmployeeById(@PathVariable Long id) {
        employeeService.deleteById(id);
    }
}

