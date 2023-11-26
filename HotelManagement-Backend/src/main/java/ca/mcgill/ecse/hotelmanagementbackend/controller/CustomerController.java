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

    @GetMapping // Updated endpoint
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/by-name/{name}") // Updated endpoint
    public List<Customer> getAllCustomersByName(@PathVariable String name) {
        return customerService.findAllByName(name);
    }

    @PostMapping // Updated endpoint
    public Long saveCustomer(@Valid @RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerService.save(customer);
        return customer.getId();
    }

    @GetMapping("/by-username/{username}") // Updated endpoint
    public Customer getCustomerByUsername(@PathVariable String username) {
        return customerService.findByUsername(username);
    }

    @GetMapping("/by-email/{email}") // Updated endpoint
    public Customer getCustomerByEmail(@PathVariable String email) {
        return customerService.findByEmail(email);
    }

    @GetMapping("/by-id/{id}") // Updated endpoint
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @DeleteMapping("/by-username/{username}") // Updated endpoint
    public void deleteCustomerByUsername(@PathVariable String username) {
        customerService.deleteByUsername(username);
    }

    @DeleteMapping("/by-email/{email}") // Updated endpoint
    public void deleteCustomerByEmail(@PathVariable String email) {
        customerService.deleteByEmail(email);
    }

    @DeleteMapping("/by-id/{id}") // Updated endpoint
    public void deleteCustomerById(@PathVariable Long id) {
        customerService.deleteById(id);
    }

}

