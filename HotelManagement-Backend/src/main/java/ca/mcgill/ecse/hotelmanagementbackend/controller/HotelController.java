package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping("/hotels")
    public List<Hotel> getAllHotels() {
        return hotelService.findAll();
    }

    @PostMapping("/hotels")
    public void saveHotel(@Valid @RequestBody Hotel hotel) {
        hotelService.save(hotel);
    }

    @GetMapping("/hotel")
    public Hotel getHotelById(@RequestParam Long id) {
        return hotelService.findById(id);
    }

    @DeleteMapping("/hotel")
    public void deleteHotelById(@RequestParam Long id) {
        hotelService.deleteById(id);
    }
}
