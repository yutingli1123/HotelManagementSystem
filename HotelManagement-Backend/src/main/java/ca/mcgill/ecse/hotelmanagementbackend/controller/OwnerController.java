package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/owners")
    public List<Owner> getAllOwners() {
        return ownerService.findAll();
    }

    @PostMapping("/owner")
    public void saveOwner(@Valid @RequestBody Owner owner) {
        ownerService.save(owner);
    }

    @GetMapping("/owner")
    public Owner getOwnerByUsername(@RequestParam String username) {
        return ownerService.findByUsername(username);
    }

    @GetMapping("/owner")
    public Owner getOwnerByEmail(@RequestParam String email) {
        return ownerService.findByEmail(email);
    }

    @GetMapping("/owner")
    public Owner getOwnerById(@RequestParam Long id) {
        return ownerService.findById(id);
    }

    @DeleteMapping("/owner")
    public void DeleteOwnerByUsername(@RequestParam String username) {
        ownerService.deleteByUsername(username);
    }

    @GetMapping("/owner")
    public void DeleteOwnerByEmail(@RequestParam String email) {
        ownerService.deleteByEmail(email);
    }

    @GetMapping("/owner")
    public void DeleteOwnerById(@RequestParam Long id) {
        ownerService.deleteById(id);
    }
}
