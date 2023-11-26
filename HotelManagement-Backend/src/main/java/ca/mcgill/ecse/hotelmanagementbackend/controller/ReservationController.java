package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Role;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CustomerService customerService;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Reservation>> getAllReservationsByUsername(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String username) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    if (issuer.equals(username)) {
                        Customer customer = customerService.findByUsername(username);
                        if (customer != null) {
                            return ResponseEntity.ok(reservationService.findAllByCustomer(customer));
                        } else {
                            return ResponseEntity.notFound().build();
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    Customer customer = customerService.findByUsername(username);
                    if (customer != null) {
                        return ResponseEntity.ok(reservationService.findAllByCustomer(customer));
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/by-checkin-date")
    public ResponseEntity<List<Reservation>> getAllReservationsByCheckInDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        return ResponseEntity.ok(reservationService.findAllByCustomerAndCheckInDate(customer, checkInDate));
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    return ResponseEntity.ok(reservationService.findAllByCheckInDate(checkInDate));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/by-checkout-date")
    public ResponseEntity<List<Reservation>> getAllReservationsByCheckOutDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        return ResponseEntity.ok(reservationService.findAllByCustomerAndCheckOutDate(customer, checkOutDate));
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    return ResponseEntity.ok(reservationService.findAllByCheckOutDate(checkOutDate));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<Reservation>> getAllReservationsByDateRange(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        return ResponseEntity.ok(reservationService.findAllByCustomerAndCheckInDateAndCheckOutDateRange(customer, checkInDate, checkOutDate));
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    return ResponseEntity.ok(reservationService.findAllByCheckInDateAndCheckOutDateRange(checkInDate,checkOutDate));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping
    public Long saveReservation(@Valid @RequestBody Reservation reservation) {
        reservationService.save(reservation);
        return reservation.getId();
    }

    @GetMapping("/by-id/{id}")
    public Reservation getReservation(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}

