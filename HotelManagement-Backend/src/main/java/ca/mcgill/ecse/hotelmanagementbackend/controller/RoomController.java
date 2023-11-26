package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.RoomBatch;
import ca.mcgill.ecse.hotelmanagementbackend.dto.RoomResult;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
import ca.mcgill.ecse.hotelmanagementbackend.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.findAll();
    }

    @GetMapping("/available")
    public List<Room> getAllAvailableRooms(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date checkInDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date checkOutDate) {
        List<Room> allRooms = roomService.findAll();
        List<Reservation> reservationsInRange = reservationService.findAllByCheckInDateAndCheckOutDateRange(checkInDate, checkOutDate);
        reservationsInRange.forEach((reservation -> allRooms.remove(reservation.getRoom())));
        return allRooms;
    }

    @GetMapping("/available/type")
    public List<RoomResult> getAllAvailableRoomTypes(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date checkInDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date checkOutDate) {
        List<Room> allRooms = roomService.findAll();
        List<Reservation> reservationsInRange = reservationService.findAllByCheckInDateAndCheckOutDateRange(checkInDate, checkOutDate);
        reservationsInRange.forEach((reservation -> allRooms.remove(reservation.getRoom())));
        Map<RoomType, Integer> availableRoomTypes = new HashMap<>();
        
        for (Room room : allRooms) {
            RoomType roomType = room.getType();
            Integer fee = room.getFee();

            // Add or update the room type and fee in the map
            availableRoomTypes.putIfAbsent(roomType, fee);
        }

        // Convert the map to a list of RoomResult
        List<RoomResult> roomResults = new ArrayList<>();
        for (Map.Entry<RoomType, Integer> entry : availableRoomTypes.entrySet()) {
            RoomResult result = new RoomResult(entry.getKey(),entry.getValue());
            roomResults.add(result);
        }
        return roomResults;
    }

    @PostMapping
    public Long saveRoom(@Valid @RequestBody Room room) {
        roomService.save(room);
        return room.getId();
    }

    @PostMapping("/batch")
    public void saveRoomInBatch(@RequestBody RoomBatch roomBatch) {
        Integer number = roomBatch.getNumber();
        for (int i =0; i<number;i++) {
            roomService.save(new Room(roomBatch.getRoomType(), roomBatch.getFee()));
        }
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

