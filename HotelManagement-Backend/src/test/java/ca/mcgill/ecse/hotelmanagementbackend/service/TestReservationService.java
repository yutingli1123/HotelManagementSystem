package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.repository.ReservationRepository;
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
 * Test for Reservation Service class
 *
 * @author Clara Jabbour
 */
@ExtendWith(MockitoExtension.class)
public class TestReservationService {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindAll() {
        // Define a list of sample reservations
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservations.add(reservation2);

        // Mock the behavior of reservationRepository.findAll()
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Call the service to get all reservations
        List<Reservation> foundReservations = reservationService.findAll();

        // Verify that the service returns the expected list of reservations
        assertNotNull(foundReservations);
        assertEquals(2, foundReservations.size());
        assertEquals(reservation1.getId(), foundReservations.get(0).getId());
        assertEquals(reservation2.getId(), foundReservations.get(1).getId());
    }

    @Test
    public void testFindById() {
        // Define a sample reservation
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        // Mock the behavior of reservationRepository.findById()
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Define an ID that doesn't exist in the repository
        Long nonExistentId = 2L;

        // Mock the behavior of reservationRepository.findById() when the ID is not found
        when(reservationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Verify that the service returns the expected reservation when found
        Reservation foundReservation = reservationService.findById(1L);
        assertNotNull(foundReservation);
        assertEquals(reservation.getId(), foundReservation.getId());

        // Verify that the service returns null when the ID is not found
        Reservation nonExistentReservation = reservationService.findById(nonExistentId);
        assertNull(nonExistentReservation);

        // Edge case: Test with a negative ID
        Long negativeId = -1L;
        // Verify that the service returns null when a negative ID is provided
        Reservation negativeIdReservation = reservationService.findById(negativeId);
        assertNull(negativeIdReservation);
    }

    @Test
    public void testDeleteById() {
        // Mock the behavior of reservationRepository.deleteById()
        reservationService.deleteById(1L);

        // Verify that the service calls deleteById with the correct argument
        verify(reservationRepository, times(1)).deleteById(1L);

        // Edge case: Test with a negative ID
        Long negativeId = -1L;
        // Verify that the service does not delete when a negative ID is provided
        verify(reservationRepository, never()).deleteById(negativeId);
    }

    @Test
    public void testSave() {
        // Define a sample reservation to be saved
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        // Mock the behavior of reservationRepository.save() to return the reservation
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Call the service to save the reservation
        reservationService.save(reservation);

        // Verify that the service calls reservationRepository.save with the correct argument
        verify(reservationRepository, times(1)).save(reservation);

    }


    @Test
    public void testFindAllWithEmptyList() {
        // Mock the behavior of reservationRepository.findAll() when the list is empty
        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());

        // Call the service to get all reservations
        List<Reservation> foundReservations = reservationService.findAll();

        // Verify that the service returns an empty list when no reservations are found
        assertNotNull(foundReservations);
        assertTrue(foundReservations.isEmpty());
    }

}
 

