package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/by-checkin-date")
    public List<Reservation> getAllReservationsByCheckInDate(@RequestParam Date checkInDate) {
        return reservationService.findAllByCheckInDate(checkInDate);
    }

    @GetMapping("/by-checkout-date")
    public List<Reservation> getAllReservationsByCheckOutDate(@RequestParam Date checkOutDate) {
        return reservationService.findAllByCheckOutDate(checkOutDate);
    }

    @GetMapping("/by-date-range")
    public List<Reservation> getAllReservationsByDateRange(@RequestParam Date checkInDate, @RequestParam Date checkOutDate) {
        return reservationService.findAllByCheckInDateAndCheckOutDateRange(checkInDate,checkOutDate);
    }

    @PostMapping
    public Long saveReservation(@Valid @RequestBody Reservation reservation) {
        reservationService.save(reservation);
        return reservation.getId();
    }

    @GetMapping("/by-id/{id}")
    public Reservation getReservation(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}

