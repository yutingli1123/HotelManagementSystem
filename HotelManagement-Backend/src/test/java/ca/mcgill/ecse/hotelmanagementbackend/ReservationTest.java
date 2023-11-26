package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
import ca.mcgill.ecse.hotelmanagementbackend.service.RoomService;
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
    private CustomerService customerService;
    @Autowired
    private RoomService roomService;

    @Test
    @Transactional
    void testReadAndWriteReservationObjects() {
        Reservation reservation = new Reservation(new Date());
        reservationService.save(reservation);
        assertEquals("checks if reservation is properly saved", 1, reservationService.findAll().size());
    }

    @Test
    @Transactional
    void testReadAndWriteReservationAttributes() {
        Customer customer1 = new Customer("Test1", "test1", "test1@test.com", "test1");
        Customer customer2 = new Customer("Test2", "test2", "test2@test.com", "test2");
        customerService.save(customer1);
        Reservation reservation = new Reservation(new Date());
        reservation.setCustomer(customer1);
        reservationService.save(reservation);
        //test read
        assertEquals("test read Reservation Attributes(customer)", customer1, reservation.getCustomer());
        //test write
        reservation.setCustomer(customer2);
        reservationService.save(reservation);
        assertEquals("test write Reservation Attributes(customer)", customer2, reservation.getCustomer());
    }

    @Test
    @Transactional
    void testReadAndWriteReference() {
        Reservation reservation = new Reservation(new Date(),);
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room(RoomType.DELUXE, 1000);
        roomService.save(room1);
        Room room2 = new Room(RoomType.REGULAR, 1000);
        roomService.save(room2);
        Room room3 = new Room(RoomType.DOUBLE, 1000);
        roomService.save(room3);
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        // test read and write reference between room reservation
        reservation.setRooms(rooms);
        reservationService.save(reservation);
        assertEquals("checks if reservation can read and write its attribute which is an instance of Room class", rooms, reservation.getRooms());
    }
}
