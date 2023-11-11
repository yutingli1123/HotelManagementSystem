package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/customers") // Updated base path
public class CustomerController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16,32,1,60000,10);

    @Autowired
    private CustomerService customerService;

    @GetMapping("/all") // Updated endpoint
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @PostMapping("/create") // Updated endpoint
    public void saveCustomer(@Valid @RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerService.save(customer);
    }

    @GetMapping("/username") // Updated endpoint
    public Customer getCustomerByUsername(@RequestParam String username) {
        return customerService.findByUsername(username);
    }

    @GetMapping("/email") // Updated endpoint
    public Customer getCustomerByEmail(@RequestParam String email) {
        return customerService.findByEmail(email);
    }

    @GetMapping("/id") // Updated endpoint
    public Customer getCustomerById(@RequestParam Long id) {
        return customerService.findById(id);
    }

    @DeleteMapping("/delete/username") // Updated endpoint
    public void deleteCustomerByUsername(@RequestParam String username) {
        customerService.deleteByUsername(username);
    }

    @DeleteMapping("/delete/email") // Updated endpoint
    public void deleteCustomerByEmail(@RequestParam String email) {
        customerService.deleteByEmail(email);
    }

    @DeleteMapping("/delete/id") // Updated endpoint
    public void deleteCustomerById(@RequestParam Long id) {
        customerService.deleteById(id);
    }
}

