package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import ca.mcgill.ecse.hotelmanagementbackend.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return roomService.findAll();
    }

    @PostMapping("/room")
    public void saveRoom(@Valid @RequestBody Room room) {
        roomService.save(room);
    }

    @GetMapping("/rooms")
    public List<Room> getAllRoomsByRoomType(@RequestParam RoomType roomType) {
        return roomService.findAllByRoomType(roomType);
    }

    @DeleteMapping("/room")
    public void deleteRoom(@RequestParam Long id) {
        roomService.deleteById(id);
    }
}
