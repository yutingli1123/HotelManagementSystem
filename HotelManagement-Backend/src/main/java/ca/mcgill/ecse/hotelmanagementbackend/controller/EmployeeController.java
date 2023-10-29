package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @PostMapping("/employee")
    public void saveEmployee(@Valid @RequestBody Employee employee) {
        employeeService.save(employee);
    }

    @GetMapping("/employee")
    public Employee getEmployeeByUsername(@RequestParam String username) {
        return employeeService.findByUsername(username);
    }

    @GetMapping("/employee")
    public Employee getEmployeeByEmail(@RequestParam String email) {
        return employeeService.findByEmail(email);
    }

    @GetMapping("/employee")
    public Employee getEmployeeById(@RequestParam Long id) {
        return employeeService.findById(id);
    }

    @DeleteMapping("/employee")
    public void deleteEmployeeByUsername(@RequestParam String username) {
        employeeService.deleteByUsername(username);
    }

    @GetMapping("/employee")
    public void deleteEmployeeByEmail(@RequestParam String email) {
        employeeService.deleteByEmail(email);
    }

    @GetMapping("/employee")
    public void deleteEmployeeById(@RequestParam Long id) {
        employeeService.deleteById(id);
    }
}
