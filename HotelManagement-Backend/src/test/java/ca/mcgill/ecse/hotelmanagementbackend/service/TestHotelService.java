package ca.mcgill.ecse.hotelmanagementbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.mcgill.ecse.hotelmanagementbackend.repository.HotelRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for Hotel service class
 * @author Clara Jabbour and Sami Bayoud
 */

@ExtendWith(MockitoExtension.class)
public class TestHotelService {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindById() {
        // Define a sample hotel
        Hotel hotel = new Hotel();
        hotel.setId(1L);

        // Mock the behavior of hotelRepository.findById()
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        // Define an ID that doesn't exist in the repository
        Long nonExistentId = 2L;

        // Mock the behavior of hotelRepository.findById() when the ID is not found
        when(hotelRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Verify that the service returns the expected hotel when found
        Hotel foundHotel = hotelService.findById(1L);
        assertNotNull(foundHotel);
        assertEquals(hotel.getId(), foundHotel.getId());
   
        // Verify that the service returns null when the ID is not found
        Hotel nonExistentHotel = hotelService.findById(nonExistentId);
        assertNull(nonExistentHotel);
    }

    @Test
    public void testFindAll() {
        // Define a list of sample hotels
        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel1 = new Hotel();
        hotel1.setId(1L);
        hotels.add(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setId(2L);
        hotels.add(hotel2);

        // Mock the behavior of hotelRepository.findAll()
        when(hotelRepository.findAll()).thenReturn(hotels);

        // Call the service to get all hotels
        List<Hotel> foundHotels = hotelService.findAll();

        // Verify that the service returns the expected list of hotels
        assertNotNull(foundHotels);
        assertEquals(2, foundHotels.size());
        assertEquals(hotel1.getId(), foundHotels.get(0).getId());
        assertEquals(hotel2.getId(), foundHotels.get(1).getId());

    }

    @Test
    public void testSave() {
        // Define a sample hotel to be saved
        Hotel hotel = new Hotel();
        hotel.setId(1L);

        // Mock the behavior of hotelRepository.save() to return the hotel
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        // Call the service to save the hotel
        hotelService.save(hotel);

        // Verify that the service calls hotelRepository.save with the correct argument
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    public void testDeleteById() {
        // Mock the behavior of hotelRepository.deleteById()
        hotelService.deleteById(1L);

        // Verify that the service calls deleteById with the correct argument
        verify(hotelRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByIdWithInvalidInput() {
        // Define an invalid (negative) ID
        Long invalidId = -1L;

        // Call the service to find a hotel with an invalid ID
        Hotel foundHotel = hotelService.findById(invalidId);

        // Verify that the service returns null for an invalid input
        assertNull(foundHotel);
    }


}
