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
@RequestMapping("/api/v1")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations")
    public List<Reservation> getAllReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations")
    public List<Reservation> getAllReservationsByDate(@RequestParam Date date) {
        return reservationService.findAllByDate(date);
    }

    @PostMapping("/reservation")
    public void saveReservation(@Valid @RequestBody Reservation reservation) {
        reservationService.save(reservation);
    }

    @GetMapping("/reservation")
    public Reservation getReservation(@RequestParam Long id) {
        return reservationService.findById(id);
    }
}
