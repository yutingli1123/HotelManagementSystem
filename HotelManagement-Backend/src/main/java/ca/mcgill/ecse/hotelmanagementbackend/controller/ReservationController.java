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

    @GetMapping("/by-date-range")
    public List<Reservation> getAllReservationsByDateBetween(@RequestParam Date startDate, @RequestParam Date endDate) {
        return reservationService.findAllByDateBetween(startDate, endDate);
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

    @DeleteMapping("/by-date-range")
    public void deleteReservation(@RequestParam Date startDate, @RequestParam Date endDate) {
        reservationService.deleteAllByDateBetween(startDate, endDate);
    }
}

