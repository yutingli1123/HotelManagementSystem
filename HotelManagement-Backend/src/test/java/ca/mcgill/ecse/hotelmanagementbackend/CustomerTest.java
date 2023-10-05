package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class CustomerTest {
    @Autowired
    private CustomerService customerService;
    private HotelService hotelService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveCustomer(){
        //test if a user or multiple users can be saved into and deleted from db
        List<Customer> originalUsers = customerService.findAll();
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Customer user1 = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        Customer user2 = new Customer("Test2", "test2", "test2@test.com", "test2", hotel);
        Customer user3 = new Customer("Test3", "test3", "test3@test.com", "test3", hotel);
        customerService.save(user1);
        customerService.save(user2);
        customerService.save(user3);
        assertEquals("checks if db has three customers saved", originalUsers.size() + 3, customerService.findAll().size());
        customerService.deleteByUsername("test1");
        assertEquals("checks if db has three customers saved", originalUsers.size() + 2, customerService.findAll().size());
        customerService.deleteByUsername("test2");
        assertEquals("checks if db has three customers saved", originalUsers.size() + 1, customerService.findAll().size());
        customerService.deleteByUsername("test3");
        assertEquals("checks if db has three customers saved", originalUsers.size(), customerService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    @Test
    void testSearchCustomerByEmail(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Customer> originalUsers = customerService.findAll();
        Customer user = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(user);
        Customer userByEmail = customerService.findByEmail("test1@test.com");
        assertEquals("checks if customer searched by email matches the customer saved", user, userByEmail);
        customerService.deleteByEmail("test1@test.com");
        assertEquals("checks if customer can be deleted by email", originalUsers.size(), customerService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    @Test
    void testSearchCustomerById(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Customer> originalUsers = customerService.findAll();
        Customer user = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(user);
        Long userId = user.getId();
        Customer userById = customerService.findById(userId);
        assertEquals("checks if customer searched by id matches the customer saved", user, userById);
        customerService.deleteById(userId);
        assertEquals("checks if customer can be deleted by id", originalUsers.size(), customerService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    void testSearchCustomerByUserName(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        List<Customer> originalUsers = customerService.findAll();
        Customer user = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(user);
        Customer userByUserName = customerService.findByUsername("test1");
        assertEquals("checks if customer searched by userName matches the customer saved", user, userByUserName);
        customerService.deleteByUsername("test1");
        assertEquals("checks if customer can be deleted by userName", originalUsers.size(), customerService.findAll().size());
        hotelService.deleteById(hotel.getId());
    }

    void testCustomerReadAndWrite() {
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Customer user = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(user);
        String name = user.getName();
        assertEquals("checks if customer name can be correctly read", "Test1", name);
        user.setName("John");
        name = user.getName();
        assertEquals("checks if customer name can be correctly written", "John", name);
        customerService.deleteByUsername("test1");
        hotelService.deleteById(hotel.getId());
    }

    void testCustomerReferenceWithHotel(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Customer user = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(user);
        Hotel hotelByCustomer = user.getHotel();
        assertEquals("checks if the customer class can successfully find the hotel class", hotel, hotelByCustomer);
    }
}
