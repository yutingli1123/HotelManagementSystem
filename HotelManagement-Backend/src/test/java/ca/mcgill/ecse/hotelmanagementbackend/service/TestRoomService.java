package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import ca.mcgill.ecse.hotelmanagementbackend.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for Room Service class
 * @author Clara Jabbour
 */
@ExtendWith(MockitoExtension.class)
public class TestRoomService {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindAll() {
        // Define a list of sample rooms
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room();
        room1.setId(1L);
        rooms.add(room1);

        Room room2 = new Room();
        room2.setId(2L);
        rooms.add(room2);

        // Mock the behavior of roomRepository.findAll()
        when(roomRepository.findAll()).thenReturn(rooms);

        // Call the service to get all rooms
        List<Room> foundRooms = roomService.findAll();

        // Verify that the service returns the expected list of rooms
        assertNotNull(foundRooms);
        assertEquals(2, foundRooms.size());
        assertEquals(room1.getId(), foundRooms.get(0).getId());
        assertEquals(room2.getId(), foundRooms.get(1).getId());
    }

    @Test
    public void testFindAllByRoomType() {
        // Define a sample room type
        RoomType roomType = RoomType.REGULAR;

        // Define a list of sample rooms with the specified room type
        List<Room> rooms = new ArrayList<>();
        Room room1 = new Room();
        room1.setId(1L);
        room1.setType(roomType);
        rooms.add(room1);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setType(roomType);
        rooms.add(room2);

        // Mock the behavior of roomRepository.findAllByType()
        when(roomRepository.findAllByType(roomType)).thenReturn(Optional.of(rooms));

        // Call the service to get all rooms by the specified room type
        List<Room> foundRooms = roomService.findAllByRoomType(roomType);

        // Verify that the service returns the expected list of rooms
        assertNotNull(foundRooms);
        assertEquals(2, foundRooms.size());
        assertEquals(room1.getId(), foundRooms.get(0).getId());
        assertEquals(room2.getId(), foundRooms.get(1).getId());
    }

    @Test
    public void testSave() {
        // Define a sample room to be saved
        Room room = new Room();
        room.setId(1L);

        // Mock the behavior of roomRepository.save()
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Call the service to save the room
        roomService.save(room);

        // Verify that the service calls roomRepository.save with the correct argument
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testDeleteById() {
        // Mock the behavior of roomRepository.deleteById()
        roomService.deleteById(1L);

        // Verify that the service calls deleteById with the correct argument
        verify(roomRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAllWithEmptyList() {
        // Mock the behavior of roomRepository.findAll() when the list is empty
        when(roomRepository.findAll()).thenReturn(new ArrayList<>());

        // Call the service to get all rooms
        List<Room> foundRooms = roomService.findAll();

        // Verify that the service returns an empty list when no rooms are found
        assertNotNull(foundRooms);
        assertTrue(foundRooms.isEmpty());
    }

    @Test
    public void testFindAllByRoomTypeWithEmptyList() {
        // Define a room type
        RoomType roomType = RoomType.REGULAR;

        // Mock the behavior of roomRepository.findAllByType() when the list is empty
        when(roomRepository.findAllByType(roomType)).thenReturn(Optional.of(new ArrayList<>()));

        // Call the service to get all rooms by room type
        List<Room> foundRooms = roomService.findAllByRoomType(roomType);

        // Verify that the service returns an empty list when no rooms of the specified type are found
        assertNotNull(foundRooms);
        assertTrue(foundRooms.isEmpty());
}

    @Test
    public void testFindAllByRoomTypeWithNullType() {
        // Call the service to get all rooms by a null room type
        List<Room> foundRooms = roomService.findAllByRoomType(null);

        // Verify that the service returns null when a null room type is provided
        assertNull(foundRooms);
    }

    @Test
    public void testFindAllByRoomTypeWithNonexistentType() {
        // Define a room type that does not exist
        RoomType roomType = RoomType.REGULAR;

        // Mock the behavior of roomRepository.findAllByType() when the type is not found
        when(roomRepository.findAllByType(roomType)).thenReturn(Optional.empty());

        // Call the service to get all rooms by a room type that does not exist
        List<Room> foundRooms = roomService.findAllByRoomType(roomType);

        // Verify that the service returns null when no rooms of the specified type are found
        assertNull(foundRooms);
    }


}
