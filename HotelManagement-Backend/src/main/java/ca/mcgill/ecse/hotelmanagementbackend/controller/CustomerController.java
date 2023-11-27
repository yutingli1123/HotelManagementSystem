package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.CustomerDto;
import ca.mcgill.ecse.hotelmanagementbackend.dto.PasswordDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
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

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/customers") // Updated base path
public class CustomerController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16,32,1,60000,10);

    @Autowired
    private CustomerService customerService;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @GetMapping // Updated endpoint
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/by-name/{name}") // Updated endpoint
    public List<Customer> getAllCustomersByName(@PathVariable String name) {
        return customerService.findAllByName(name);
    }

    @GetMapping("/name") // Updated endpoint
    public ResponseEntity<String> getCustomerName(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Customer customer = customerService.findByUsername(issuer);
                if (customer!=null) {
                    return ResponseEntity.ok(customer.getName());
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/me") // Updated endpoint
    public ResponseEntity<CustomerDto> getCustomerSelf(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Customer customer = customerService.findByUsername(issuer);
                if (customer!=null) {
                    CustomerDto customerDto = new CustomerDto(customer.getName(),customer.getUsername(),customer.getEmail());
                    return ResponseEntity.ok(customerDto);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping // Updated endpoint
    public Long saveCustomer(@Valid @RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerService.save(customer);
        return customer.getId();
    }

    @PutMapping ("/update")// Updated endpoint
    public ResponseEntity<Boolean> updateCustomer(@Valid @RequestBody CustomerDto customerDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Customer customer = customerService.findByUsername(issuer);
                if (customer!=null) {
                    customer.setName(customerDto.getName());
                    customer.setEmail(customerDto.getEmail());
                    customerService.save(customer);
                    return ResponseEntity.ok(Boolean.TRUE);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping ("/update/password")// Updated endpoint
    public ResponseEntity<Boolean> updateCustomerPassword(@Valid @RequestBody PasswordDto passwordDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Customer customer = customerService.findByUsername(issuer);
                if (customer!=null) {
                    if (passwordEncoder.matches(passwordDto.getOldPass(), customer.getPassword())) {
                        customer.setPassword(passwordEncoder.encode(passwordDto.getNewPass()));
                        customerService.save(customer);
                        return ResponseEntity.ok(Boolean.TRUE);
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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

