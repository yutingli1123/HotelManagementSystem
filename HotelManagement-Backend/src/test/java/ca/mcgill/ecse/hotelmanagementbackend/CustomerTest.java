package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class CustomerTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ReservationService reservationService;


    @Test
    @Transactional
    void testSaveCustomer(){
        //test if a user or multiple users can be saved into and deleted from db
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Customer customer1 = new Customer("Test1", "test1", "test1@test.com", "test1");
        Customer customer2 = new Customer("Test2", "test2", "test2@test.com", "test2");
        Customer customer3 = new Customer("Test3", "test3", "test3@test.com", "test3");
        customerService.save(customer1);
        customerService.save(customer2);
        customerService.save(customer3);
        assertEquals("checks if db has three customers saved", 3, customerService.findAll().size());
    }

    @Test
    @Transactional
    void testSearchCustomerByEmail(){
        List<Customer> originalUsers = customerService.findAll();
        Customer customer = new Customer("Test1", "test1", "test1@test.com", "test1");
        customerService.save(customer);
        Customer customerByEmail = customerService.findByEmail("test1@test.com");
        assertEquals("checks if customer searched by email matches the customer saved", customer, customerByEmail);
        customerService.deleteByEmail("test1@test.com");
        assertEquals("checks if customer can be deleted by email", originalUsers.size(), customerService.findAll().size());
    }

    @Test
    @Transactional
    void testSearchCustomerById(){
        List<Customer> originalUsers = customerService.findAll();
        Customer customer = new Customer("Test1", "test1", "test1@test.com", "test1");
        customerService.save(customer);
        Long customerId = customer.getId();
        Customer userById = customerService.findById(customerId);
        assertEquals("checks if customer searched by id matches the customer saved", customer, userById);
        customerService.deleteById(customerId);
        assertEquals("checks if customer can be deleted by id", originalUsers.size(), customerService.findAll().size());
    }

    @Test
    @Transactional
        void testSearchCustomerByUserName(){
        List<Customer> originalUsers = customerService.findAll();
        Customer customer = new Customer("Test1", "test1", "test1@test.com", "test1");
        customerService.save(customer);
        Customer userByUserName = customerService.findByUsername("test1");
        assertEquals("checks if customer searched by userName matches the customer saved", customer, userByUserName);
        customerService.deleteByUsername("test1");
        assertEquals("checks if customer can be deleted by userName", originalUsers.size(), customerService.findAll().size());
    }

    @Test
    @Transactional
    void testCustomerReadAndWrite() {
        Customer customer = new Customer("Test1", "test1", "test1@test.com", "test1");
        customerService.save(customer);
        String name = customer.getName();
        assertEquals("checks if customer name can be correctly read", "Test1", name);
        customer.setName("John");
        name = customer.getName();
        assertEquals("checks if customer name can be correctly written", "John", name);
        customerService.deleteByUsername("test1");
    }

    @Test
    @Transactional
    void testCustomerReference(){
        List<Reservation> reservationList = new ArrayList<>();
        //read
        Customer customer = new Customer("Test1", "test1", "test1@test.com", "test1");
        customerService.save(customer);
        //write
        Date today = new Date();
        Room room1 = new Room(RoomType.DELUXE,1000);
        Reservation reservation = new Reservation(today, new Date(today.getTime() + 24 * 60 * 60 * 1000),room1);
        reservationService.save(reservation);
        reservationList.add(reservation);
        customer.setReservationsForCustomer(reservationList);
        customerService.save(customer);
        assertEquals("checks if user can write its attribute which is an instance of Reservation class", reservationList, customer.getReservationsForCustomer());
    }
}
