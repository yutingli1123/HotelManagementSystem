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
@RequestMapping("/api/v1")
public class CustomerController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16,32,1,60000,10);

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @PostMapping("/customer")
    public void saveCustomer(@Valid @RequestBody Customer customer) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerService.save(customer);
    }

    @GetMapping("/customer")
    public Customer getCustomerByUsername(@RequestParam String username) {
        return customerService.findByUsername(username);
    }

    @GetMapping("/customer")
    public Customer getCustomerByEmail(@RequestParam String email) {
        return customerService.findByEmail(email);
    }

    @GetMapping("/customer")
    public Customer getCustomerById(@RequestParam Long id) {
        return customerService.findById(id);
    }

    @DeleteMapping("/customer")
    public void deleteCustomerByUsername(@RequestParam String username) {
        customerService.deleteByUsername(username);
    }
    @DeleteMapping("/customer")
    public void deleteCustomerByEmail(@RequestParam String email) {
        customerService.deleteByEmail(email);
    }
    @DeleteMapping("/customer")
    public void deleteCustomerById(@RequestParam Long id) {
        customerService.deleteById(id);
    }
}
