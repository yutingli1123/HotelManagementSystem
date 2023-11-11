package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

