package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/owner")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/all")
    public List<Owner> getAllOwners() {
        return ownerService.findAll();
    }

    @PostMapping("/save")
    public void saveOwner(@Valid @RequestBody Owner owner) {
        ownerService.save(owner);
    }

    @GetMapping("/by-username")
    public Owner getOwnerByUsername(@RequestParam String username) {
        return ownerService.findByUsername(username);
    }

    @GetMapping("/by-email")
    public Owner getOwnerByEmail(@RequestParam String email) {
        return ownerService.findByEmail(email);
    }

    @GetMapping("/by-id")
    public Owner getOwnerById(@RequestParam Long id) {
        return ownerService.findById(id);
    }

    @DeleteMapping("/by-username")
    public void DeleteOwnerByUsername(@RequestParam String username) {
        ownerService.deleteByUsername(username);
    }

    @DeleteMapping("/by-email")
    public void DeleteOwnerByEmail(@RequestParam String email) {
        ownerService.deleteByEmail(email);
    }

    @DeleteMapping("/by-id")
    public void DeleteOwnerById(@RequestParam Long id) {
        ownerService.deleteById(id);
    }
}

