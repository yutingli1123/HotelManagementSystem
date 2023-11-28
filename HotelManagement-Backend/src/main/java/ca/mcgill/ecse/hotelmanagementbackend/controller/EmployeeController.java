package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.EmployeeDto;
import ca.mcgill.ecse.hotelmanagementbackend.dto.PasswordDto;
import ca.mcgill.ecse.hotelmanagementbackend.dto.UpdateUserDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Role;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    @Autowired
    private EmployeeService employeeService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/ids")
    public List<EmployeeDto> getAllEmployeeIds() {
        List<Employee> employees = employeeService.findAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employees.forEach(employee -> employeeDtos.add(new EmployeeDto(employee.getId(),employee.getName())));
        return employeeDtos;
    }

    @GetMapping("/by-name/{name}") // Updated endpoint
    public List<Employee> getAllEmployeesByName(@PathVariable String name) {
        return employeeService.findAllByName(name);
    }

    @PostMapping
    public Long saveEmployee(@Valid @RequestBody Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeService.save(employee);
        return employee.getId();
    }

    @PutMapping("/update")// Updated endpoint
    public ResponseEntity<Boolean> updateCustomer(@Valid @RequestBody UpdateUserDto updateUserDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                    Employee employee = employeeService.findByUsername(issuer);
                    if (employee != null) {
                        employee.setName(updateUserDto.getName());
                        employee.setEmail(updateUserDto.getEmail());
                        employee.setSalary(updateUserDto.getSalary());
                        employeeService.save(employee);
                        return ResponseEntity.ok(Boolean.TRUE);
                    }
                } else if (role == Role.EMPLOYEE || role == Role.OWNER) {
                    Employee employee = employeeService.findByUsername(updateUserDto.getUsername());
                    if (employee != null) {
                        employee.setName(updateUserDto.getName());
                        employee.setEmail(updateUserDto.getEmail());
                        employee.setSalary(updateUserDto.getSalary());
                        employeeService.save(employee);
                        return ResponseEntity.ok(Boolean.TRUE);
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/update/password")// Updated endpoint
    public ResponseEntity<Boolean> updateCustomerPassword(@Valid @RequestBody PasswordDto passwordDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                    Employee employee= employeeService.findByUsername(issuer);
                    if (employee != null) {
                        if (passwordEncoder.matches(passwordDto.getOldPass(), employee.getPassword())) {
                            employee.setPassword(passwordEncoder.encode(passwordDto.getNewPass()));
                            employeeService.save(employee);
                            return ResponseEntity.ok(Boolean.TRUE);
                        }
                    }
                } else if (role == Role.EMPLOYEE || role == Role.OWNER) {
                    Employee employee = employeeService.findByUsername(passwordDto.getUsername());
                    if (employee != null) {
                        employee.setPassword(passwordEncoder.encode(passwordDto.getNewPass()));
                        employeeService.save(employee);
                        return ResponseEntity.ok(Boolean.TRUE);
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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

