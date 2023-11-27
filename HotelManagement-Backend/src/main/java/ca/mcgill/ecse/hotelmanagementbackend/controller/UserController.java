package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.UserDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.AccountType;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<Customer> customerList = customerService.findAll();
        List<Employee> employeeList = employeeService.findAll();
        List<Owner> ownerList = ownerService.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (Customer customer: customerList) {
            userDtos.add(new UserDto(customer.getId(),AccountType.CUSTOMER,customer.getName(),customer.getUsername(),customer.getEmail()));
        }
        for (Employee employee: employeeList) {
            userDtos.add(new UserDto(employee.getId(),AccountType.EMPLOYEE,employee.getName(),employee.getUsername(),employee.getEmail()));
        }
        for (Owner owner: ownerList) {
            userDtos.add(new UserDto(owner.getId(),AccountType.OWNER,owner.getName(),owner.getUsername(),owner.getEmail()));
        }
        return userDtos;
    }
}
