package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class OwnerTest {
    @Autowired
    private OwnerService ownerService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveOwner() {
        List<Owner> originalOwners = ownerService.findAll();
        Owner owner1 = new Owner("Owner1", "owner1", "owner1@test.com", "owner1");
        Owner owner2 = new Owner("Owner2", "owner2", "owner2@test.com", "owner2");
        Owner owner3 = new Owner("Owner3", "owner3", "owner3@test.com", "owner3");
        ownerService.save(owner1);
        ownerService.save(owner2);
        ownerService.save(owner3);
        assertEquals("checks if db has three owners saved", originalOwners.size() + 3, ownerService.findAll().size());
        ownerService.deleteByUsername("owner1");
        assertEquals("checks if db has two owners saved", originalOwners.size() + 2, ownerService.findAll().size());
        ownerService.deleteByUsername("owner2");
        assertEquals("checks if db has one owner saved", originalOwners.size() + 1, ownerService.findAll().size());
        ownerService.deleteByUsername("owner3");
        assertEquals("checks if db has the original owners saved", originalOwners.size(), ownerService.findAll().size());
    }

    @Test
    void testSearchOwnerByEmail() {
        List<Owner> originalOwners = ownerService.findAll();
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1");
        ownerService.save(owner);
        Owner ownerByEmail = ownerService.findByEmail("owner1@test.com");
        assertEquals("checks if owner searched by email matches the owner saved", owner, ownerByEmail);
        ownerService.deleteByEmail("owner1@test.com");
        assertEquals("checks if owner can be deleted by email", originalOwners.size(), ownerService.findAll().size());
    }

    @Test
    void testSearchOwnerById() {
        List<Owner> originalOwners = ownerService.findAll();
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1");
        ownerService.save(owner);
        Long ownerId = owner.getId();
        Owner ownerById = ownerService.findById(ownerId);
        assertEquals("checks if owner searched by id matches the owner saved", owner, ownerById);
        ownerService.deleteById(ownerId);
        assertEquals("checks if owner can be deleted by id", originalOwners.size(), ownerService.findAll().size());
    }

    @Test
    void testSearchOwnerByUserName() {
        List<Owner> originalOwners = ownerService.findAll();
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1");
        ownerService.save(owner);
        Owner ownerByUserName = ownerService.findByUsername("owner1");
        assertEquals("checks if owner searched by userName matches the owner saved", owner, ownerByUserName);
        ownerService.deleteByUsername("owner1");
        assertEquals("checks if owner can be deleted by userName", originalOwners.size(), ownerService.findAll().size());
    }

    @Test
    void testOwnerReadAndWrite() {
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1");
        ownerService.save(owner);
        String name = owner.getName();
        assertEquals("checks if owner name can be correctly read", "Owner1", name);
        owner.setName("John");
        name = owner.getName();
        assertEquals("checks if owner name can be correctly written", "John", name);
        ownerService.deleteByUsername("owner1");
    }
}
