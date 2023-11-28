package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.PasswordDto;
import ca.mcgill.ecse.hotelmanagementbackend.dto.UpdateUserDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Role;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
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
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ReservationService reservationService;

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
                if (customerService.findByUsername(issuer) != null) {
                    return ResponseEntity.ok(customerService.findByUsername(issuer).getName());
                } else if (employeeService.findByUsername(issuer) != null) {
                    return ResponseEntity.ok(employeeService.findByUsername(issuer).getName());
                } else if (ownerService.findByUsername(issuer) != null) {
                    return ResponseEntity.ok(ownerService.findByUsername(issuer).getName());
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/me") // Updated endpoint
    public ResponseEntity<UpdateUserDto> getCustomerSelf(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Customer customer = customerService.findByUsername(issuer);
                if (customer != null) {
                    UpdateUserDto updateUserDto = new UpdateUserDto(customer.getName(), customer.getUsername(), customer.getEmail(),null);
                    return ResponseEntity.ok(updateUserDto);
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

    @PutMapping("/update")// Updated endpoint
    public ResponseEntity<Boolean> updateCustomer(@Valid @RequestBody UpdateUserDto updateUserDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        customer.setName(updateUserDto.getName());
                        customer.setEmail(updateUserDto.getEmail());
                        customerService.save(customer);
                        return ResponseEntity.ok(Boolean.TRUE);
                    }
                } else if (role == Role.EMPLOYEE || role == Role.OWNER) {
                    Customer customer = customerService.findByUsername(updateUserDto.getUsername());
                    if (customer != null) {
                        customer.setName(updateUserDto.getName());
                        customer.setEmail(updateUserDto.getEmail());
                        customerService.save(customer);
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
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        if (passwordEncoder.matches(passwordDto.getOldPass(), customer.getPassword())) {
                            customer.setPassword(passwordEncoder.encode(passwordDto.getNewPass()));
                            customerService.save(customer);
                            return ResponseEntity.ok(Boolean.TRUE);
                        }
                    }
                } else if (role == Role.EMPLOYEE || role == Role.OWNER) {
                    Customer customer = customerService.findByUsername(passwordDto.getUsername());
                    if (customer != null) {
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
        Customer customer = customerService.findByUsername(username);
        List<Reservation> reservations = customer.getReservationsForCustomer();
        reservations.forEach(reservation -> reservationService.deleteById(reservation.getId()));
        customerService.deleteByUsername(username);
    }

    @DeleteMapping("/by-email/{email}") // Updated endpoint
    public void deleteCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.findByEmail(email);
        List<Reservation> reservations = customer.getReservationsForCustomer();
        reservations.forEach(reservation -> reservationService.deleteById(reservation.getId()));
        customerService.deleteByEmail(email);
    }

    @DeleteMapping("/by-id/{id}") // Updated endpoint
    public void deleteCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        List<Reservation> reservations = customer.getReservationsForCustomer();
        reservations.forEach(reservation -> reservationService.deleteById(reservation.getId()));
        customerService.deleteById(id);
    }

}

