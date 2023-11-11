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
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.findAll();
    }

    @PostMapping
    public Long saveRoom(@Valid @RequestBody Room room) {
        roomService.save(room);
        return room.getId();
    }

    @GetMapping("/by-type/{roomType}")
    public List<Room> getAllRoomsByRoomType(@PathVariable RoomType roomType) {
        return roomService.findAllByRoomType(roomType);
    }

    @GetMapping("/by-id/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
    }
}

