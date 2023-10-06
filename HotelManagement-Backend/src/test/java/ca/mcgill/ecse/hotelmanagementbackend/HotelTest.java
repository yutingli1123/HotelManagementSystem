package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.User;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class HotelTest {
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private CustomerService customerService;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void testReadAndWriteHotelObjects() {
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Long hotelId = hotel.getId();
        Hotel hotelById = hotelService.findById(hotelId);
        assertEquals("checks if hotel is saved", hotel, hotelById);
    }
    @Test
    @Transactional
    void testReadAndWriteHotelAttributes() {
        Hotel hotel = new Hotel("08:00:00", "23:30:00");
        hotelService.save(hotel);
        // checks read
        String openTime = hotel.getOpenTime();
        String closeTime = hotel.getCloseTime();
        assertEquals("checks open time read", "08:00:00", openTime);
        assertEquals("checks close time read", "23:30:00", closeTime);
        // check write
        hotel.setOpenTime("06:00:00");
        hotel.setCloseTime("00:00:00");
        openTime = hotel.getOpenTime();
        closeTime = hotel.getCloseTime();
        assertEquals("checks open time written", "06:00:00", openTime);
        assertEquals("checks close time written", "00:00:00", closeTime);
    }

    @Test
    @Transactional
    void testReadAndWriteReferenceWithUser(){
        Hotel hotel = new Hotel("08:00:00", "23:30:00");
        hotelService.save(hotel);
        Owner owner = new Owner("Owner1", "owner1", "owner1@test.com", "owner1", hotel);
        ownerService.save(owner);
        Customer customer = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(customer);
        List<User> users = new ArrayList<>();
        users.add(owner);
        users.add(customer);
        //test read and write
        hotel.setUsers(users);
        hotelService.save(hotel);
        assertEquals("checks if a hotel can get its user", users, hotel.getUsers());
    }
}
