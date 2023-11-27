package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.LoginData;
import ca.mcgill.ecse.hotelmanagementbackend.dto.RegisterData;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Role;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/v1") // Updated base path
public class LoginController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<HashMap<String,String>> login(@RequestBody LoginData loginData) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + 15 * 60 * 1000);
        Date refresh_expire = new Date(now.getTime() + 20 * 60 * 1000);
        String username = loginData.getUsername();
        String rawPassword = loginData.getPassword();

        if (customerService.findByUsername(username) != null) {
            String password = customerService.findByUsername(username).getPassword();
            if (passwordEncoder.matches(rawPassword, password)) {
                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("token",JWT.create().withIssuer(username).withClaim("role", Role.CUSTOMER.toString()).withIssuedAt(now).withExpiresAt(expire).sign(Algorithm.HMAC256(secretKey)));
                responseData.put("refresh_token", JWT.create().withIssuer(username).withIssuedAt(now).withExpiresAt(refresh_expire).sign(Algorithm.HMAC256(secretKey)));
                return ResponseEntity.ok(responseData);
            }
        } else if (employeeService.findByUsername(username) != null) {
            String password = employeeService.findByUsername(username).getPassword();
            if (passwordEncoder.matches(rawPassword, password)) {
                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("token",JWT.create().withIssuer(username).withClaim("role", Role.EMPLOYEE.toString()).withIssuedAt(now).withExpiresAt(expire).sign(Algorithm.HMAC256(secretKey)));
                responseData.put("refresh_token", JWT.create().withIssuer(username).withIssuedAt(now).withExpiresAt(refresh_expire).sign(Algorithm.HMAC256(secretKey)));
                return ResponseEntity.ok(responseData);
            }
        } else if (ownerService.findByUsername(username) != null) {
            String password = ownerService.findByUsername(username).getPassword();
            if (passwordEncoder.matches(rawPassword, password)) {
                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("token",JWT.create().withIssuer(username).withClaim("role", Role.OWNER.toString()).withIssuedAt(now).withExpiresAt(expire).sign(Algorithm.HMAC256(secretKey)));
                responseData.put("refresh_token", JWT.create().withIssuer(username).withIssuedAt(now).withExpiresAt(refresh_expire).sign(Algorithm.HMAC256(secretKey)));
                return ResponseEntity.ok(responseData);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterData registerData) {
        if (customerService.findByUsername(registerData.getUsername()) == null) {
            Customer customer = new Customer(registerData.getName(), registerData.getUsername(), registerData.getEmail(), passwordEncoder.encode(registerData.getPassword()));
            customerService.save(customer);
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<HashMap<String,String>> register(@RequestBody String refreshToken) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + 15 * 60 * 1000);
        Date refresh_expire = new Date(now.getTime() + 20 * 60 * 1000);
        String username = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(refreshToken).getIssuer();

        if (customerService.findByUsername(username) != null) {
                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("token",JWT.create().withIssuer(username).withClaim("role", Role.CUSTOMER.toString()).withIssuedAt(now).withExpiresAt(expire).sign(Algorithm.HMAC256(secretKey)));
                responseData.put("refresh_token", JWT.create().withIssuer(username).withIssuedAt(now).withExpiresAt(refresh_expire).sign(Algorithm.HMAC256(secretKey)));
                return ResponseEntity.ok(responseData);
        } else if (employeeService.findByUsername(username) != null) {

                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("token",JWT.create().withIssuer(username).withClaim("role", Role.EMPLOYEE.toString()).withIssuedAt(now).withExpiresAt(expire).sign(Algorithm.HMAC256(secretKey)));
                responseData.put("refresh_token", JWT.create().withIssuer(username).withIssuedAt(now).withExpiresAt(refresh_expire).sign(Algorithm.HMAC256(secretKey)));
                return ResponseEntity.ok(responseData);
        } else if (ownerService.findByUsername(username) != null) {

                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("token",JWT.create().withIssuer(username).withClaim("role", Role.OWNER.toString()).withIssuedAt(now).withExpiresAt(expire).sign(Algorithm.HMAC256(secretKey)));
                responseData.put("refresh_token", JWT.create().withIssuer(username).withIssuedAt(now).withExpiresAt(refresh_expire).sign(Algorithm.HMAC256(secretKey)));
                return ResponseEntity.ok(responseData);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
