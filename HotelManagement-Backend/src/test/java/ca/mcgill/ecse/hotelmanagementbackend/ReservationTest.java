package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class ReservationTest {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private CustomerService customerService;

    @Test
    @Transactional
    void testReadAndWriteReservationObjects() {
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Customer customer1 = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(customer1);
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation(new Date());
        reservationService.save(reservation);
        reservations.add(reservation);
        assertEquals("checks if reservation is properly saved", reservations, customer1.getReservations());
    }

    @Test
    @Transactional
    void testReadAndWriteReservationAttributes(){
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Customer customer1 = new Customer("Test1", "test1", "test1@test.com", "test1", hotel);
        customerService.save(customer1);
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation(new Date());
        //test read
        assertEquals("test read Reservation Attributes(id)", customer1.getId(), reservation.set);
    }

}
