package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.PasswordDto;
import ca.mcgill.ecse.hotelmanagementbackend.dto.UpdateUserDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Role;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
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
@RequestMapping("/api/v1/owners")
public class OwnerController {
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);

    @Autowired
    private OwnerService ownerService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @GetMapping
    public List<Owner> getAllOwners() {
        return ownerService.findAll();
    }

    @GetMapping("/by-name/{name}") // Updated endpoint
    public List<Owner> getAllOwnersByName(@PathVariable String name) {
        return ownerService.findAllByName(name);
    }

    @PostMapping
    public Long saveOwner(@Valid @RequestBody Owner owner) {
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        ownerService.save(owner);
        return owner.getId();
    }

    @PutMapping("/update")// Updated endpoint
    public ResponseEntity<Boolean> updateCustomer(@Valid @RequestBody UpdateUserDto updateUserDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                    Owner owner = ownerService.findByUsername(issuer);
                    if (owner != null) {
                        owner.setName(updateUserDto.getName());
                        owner.setEmail(updateUserDto.getEmail());
                        ownerService.save(owner);
                        return ResponseEntity.ok(Boolean.TRUE);
                    }
                } else if (role == Role.EMPLOYEE || role == Role.OWNER) {
                    Owner owner = ownerService.findByUsername(updateUserDto.getUsername());
                    if (owner != null) {
                        owner.setName(updateUserDto.getName());
                        owner.setEmail(updateUserDto.getEmail());
                        ownerService.save(owner);
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
                    Owner owner = ownerService.findByUsername(issuer);
                    if (owner != null) {
                        if (passwordEncoder.matches(passwordDto.getOldPass(), owner.getPassword())) {
                            owner.setPassword(passwordEncoder.encode(passwordDto.getNewPass()));
                            ownerService.save(owner);
                            return ResponseEntity.ok(Boolean.TRUE);
                        }
                    }
                } else if (role == Role.EMPLOYEE || role == Role.OWNER) {
                    Owner owner = ownerService.findByUsername(passwordDto.getUsername());
                    if (owner != null) {
                        owner.setPassword(passwordEncoder.encode(passwordDto.getNewPass()));
                        ownerService.save(owner);
                        return ResponseEntity.ok(Boolean.TRUE);
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/by-username/{username}")
    public Owner getOwnerByUsername(@PathVariable String username) {
        return ownerService.findByUsername(username);
    }

    @GetMapping("/by-email/{email}")
    public Owner getOwnerByEmail(@PathVariable String email) {
        return ownerService.findByEmail(email);
    }

    @GetMapping("/by-id/{id}")
    public Owner getOwnerById(@PathVariable Long id) {
        return ownerService.findById(id);
    }

    @DeleteMapping("/by-username/{username}")
    public void DeleteOwnerByUsername(@PathVariable String username) {
        ownerService.deleteByUsername(username);
    }

    @DeleteMapping("/by-email/{email}")
    public void DeleteOwnerByEmail(@PathVariable String email) {
        ownerService.deleteByEmail(email);
    }

    @DeleteMapping("/by-id/{id}")
    public void DeleteOwnerById(@PathVariable Long id) {
        ownerService.deleteById(id);
    }
}

