package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.repository.TimeTableRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * test for the time table service class
 * 
 * @author Clara Jabbour
 */
@ExtendWith(MockitoExtension.class)
public class TestTimeTableService {

    @Mock
    private TimeTableRepository timeTableRepository;

    @InjectMocks
    private TimeTableService timeTableService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindAll() {
        // Define a list of sample time tables
        List<TimeTable> timeTables = new ArrayList<>();
        TimeTable timeTable1 = new TimeTable();
        timeTable1.setId(1L);
        timeTables.add(timeTable1);

        TimeTable timeTable2 = new TimeTable();
        timeTable2.setId(2L);
        timeTables.add(timeTable2);

        // Mock the behavior of timeTableRepository.findAll()
        when(timeTableRepository.findAll()).thenReturn(timeTables);

        // Call the service to get all time tables
        List<TimeTable> foundTimeTables = timeTableService.findAll();

        // Verify that the service returns the expected list of time tables
        assertNotNull(foundTimeTables);
        assertEquals(2, foundTimeTables.size());
        assertEquals(timeTable1.getId(), foundTimeTables.get(0).getId());
        assertEquals(timeTable2.getId(), foundTimeTables.get(1).getId());

        // Add edge case: Test when the repository returns an empty list
        when(timeTableRepository.findAll()).thenReturn(new ArrayList<>());
        List<TimeTable> emptyTimeTables = timeTableService.findAll();
        assertNotNull(emptyTimeTables);
        assertTrue(emptyTimeTables.isEmpty());

        // Ensure cleanup for mocked methods
        verify(timeTableRepository, times(2)).findAll();
        verifyNoMoreInteractions(timeTableRepository); // Ensure no unexpected interactions
    }

    @Test
    public void testFindById() {
        // Define a sample time table
        TimeTable timeTable = new TimeTable();
        timeTable.setId(1L);

        // Mock the behavior of timeTableRepository.findById()
        when(timeTableRepository.findById(1L)).thenReturn(Optional.of(timeTable));

        // Define an ID that doesn't exist in the repository
        Long nonExistentId = 2L;

        // Mock the behavior of timeTableRepository.findById() when the ID is not found
        when(timeTableRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Verify that the service returns the expected time table when found
        TimeTable foundTimeTable = timeTableService.findById(1L);
        assertNotNull(foundTimeTable);
        assertEquals(timeTable.getId(), foundTimeTable.getId());

        // Verify that the service returns null when the ID is not found
        TimeTable nonExistentTimeTable = timeTableService.findById(nonExistentId);
        assertNull(nonExistentTimeTable);

        // Add edge case: Test with a negative ID
        Long negativeId = -1L;
        TimeTable negativeIdTimeTable = timeTableService.findById(negativeId);
        assertNull(negativeIdTimeTable);

        // Ensure cleanup for mocked methods
        verify(timeTableRepository, times(1)).findById(1L);
        verify(timeTableRepository, times(1)).findById(nonExistentId);
        verifyNoMoreInteractions(timeTableRepository); // Ensure no unexpected interactions
    }

    @Test
    public void testSave() {
        // Define a sample time table to be saved
        TimeTable timeTable = new TimeTable();
        timeTable.setId(1L);

        // Mock the behavior of timeTableRepository.save()
        doNothing().when(timeTableRepository).save(any(TimeTable.class));

        // Call the service to save the time table
        timeTableService.save(timeTable);

        // Verify that the service calls timeTableRepository.save with the correct argument
        verify(timeTableRepository, times(1)).save(timeTable);

        // Add edge case: Test saving a null time table
        timeTableService.save(null);
        verify(timeTableRepository, times(0)).save(null);

        // Ensure cleanup for mocked methods
        verifyNoMoreInteractions(timeTableRepository); // Ensure no unexpected interactions
    }

    @Test
    public void testDeleteById() {
        // Mock the behavior of timeTableRepository.deleteById()
        
        // Verify that the service calls deleteById with the correct argument
        timeTableService.deleteById(1L);
        verify(timeTableRepository, times(1)).deleteById(1L);

        // Add edge case: Test deleting with a negative ID
        Long negativeId = -1L;
        timeTableService.deleteById(negativeId);
        verify(timeTableRepository, times(1)).deleteById(negativeId);

        // Ensure cleanup for mocked methods
        verifyNoMoreInteractions(timeTableRepository); // Ensure no unexpected interactions
    }


    @Test
    public void testFindAllWithEmptyList() {
        // Mock the behavior of timeTableRepository.findAll() when the list is empty
        when(timeTableRepository.findAll()).thenReturn(new ArrayList<>());

        // Call the service to get all time tables
        List<TimeTable> foundTimeTables = timeTableService.findAll();

        // Verify that the service returns an empty list when no time tables are found
        assertNotNull(foundTimeTables);
        assertTrue(foundTimeTables.isEmpty());
    }
}

