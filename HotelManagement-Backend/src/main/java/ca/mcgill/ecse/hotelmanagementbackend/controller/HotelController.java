package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/hotels")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.findAll();
    }

    @PostMapping
    public Long saveHotel(@Valid @RequestBody Hotel hotel) {
        hotelService.save(hotel);
        return hotel.getId();
    }

    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteHotelById(@PathVariable Long id) {
        hotelService.deleteById(id);
    }
}
