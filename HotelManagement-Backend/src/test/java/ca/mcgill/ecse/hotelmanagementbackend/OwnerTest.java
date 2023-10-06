package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class OwnerTest {
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private HotelService hotelService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveOwner(){
        List<Owner> originalOwners = ownerService.findAll();
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Owner owner1 = new Owner("Owner1", "owner1", "owner1@test.com", "owner1", hotel);
        Owner owner2 = new Owner("Owner2", "owner2", "owner2@test.com", "owner2", hotel);
        Owner owner3 = new Owner("Owner3", "owner3", "owner3@test.com", "owner3", hotel);
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
        hotelService.deleteById(hotel.getId());
    }

    @Test
    void testSearchOwnerByEmail(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Owner> originalOwners = ownerService.findAll();
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1", hotel);
        ownerService.save(owner);
        Owner ownerByEmail = ownerService.findByEmail("owner1@test.com");
        assertEquals("checks if owner searched by email matches the owner saved", owner, ownerByEmail);
        ownerService.deleteByEmail("owner1@test.com");
        assertEquals("checks if owner can be deleted by email", originalOwners.size(), ownerService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    @Test
    void testSearchOwnerById(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Owner> originalOwners = ownerService.findAll();
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1", hotel);
        ownerService.save(owner);
        Long ownerId = owner.getId();
        Owner ownerById = ownerService.findById(ownerId);
        assertEquals("checks if owner searched by id matches the owner saved", owner, ownerById);
        ownerService.deleteById(ownerId);
        assertEquals("checks if owner can be deleted by id", originalOwners.size(), ownerService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    void testSearchOwnerByUserName(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Owner> originalOwners = ownerService.findAll();
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1", hotel);
        ownerService.save(owner);
        Owner ownerByUserName = ownerService.findByUsername("owner1");
        assertEquals("checks if owner searched by userName matches the owner saved", owner, ownerByUserName);
        ownerService.deleteByUsername("owner1");
        assertEquals("checks if owner can be deleted by userName", originalOwners.size(), ownerService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    void testOwnerReadAndWrite() {
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1", hotel);
        ownerService.save(owner);
        String name = owner.getName();
        assertEquals("checks if owner name can be correctly read", "Owner1", name);
        owner.setName("John");
        name = owner.getName();
        assertEquals("checks if owner name can be correctly written", "John", name);
        ownerService.deleteByUsername("owner1");
        hotelService.deleteById(hotel.getId());
    }

    void testOwnerReferenceWithHotel(){
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        hotelService.save(hotel1);
        //test read
        Owner owner = new Owner("Test1", "test1", "test1@test.com", "test1", hotel1);
        ownerService.save(owner);
        Hotel hotelByOwner =  owner.getHotel();
        assertEquals("checks if the employee class can successfully find the hotel class", hotel1, hotelByOwner);
        //test write
        owner.setHotel(hotel2);
        ownerService.save(owner);
        assertEquals("checks if owner can write its attribute which is an instance of Hotel class", "hotel2", owner.getHotel());
    }
}
