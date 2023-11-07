package ca.mcgill.ecse.hotelmanagementbackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ca.mcgill.ecse.hotelmanagementbackend.repository.TaskRepository;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test for Task Service class
 * @author Clara Jabbour
 */
@ExtendWith(MockitoExtension.class)
public class TestTaskService {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindAll() {
        // Define a list of sample tasks
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setId(1L);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setId(2L);
        tasks.add(task2);

        // Mock the behavior of taskRepository.findAll()
        when(taskRepository.findAll()).thenReturn(tasks);

        // Call the service to get all tasks
        List<Task> foundTasks = taskService.findAll();

        // Verify that the service returns the expected list of tasks
        assertNotNull(foundTasks);
        assertEquals(2, foundTasks.size());
        assertEquals(task1.getId(), foundTasks.get(0).getId());
        assertEquals(task2.getId(), foundTasks.get(1).getId());
    }

    @Test
    public void testFindAllByStartDate() {
        // Define a sample start date
        Date startDate = new Date();

        // Define a list of sample tasks with the specified start date
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setId(1L);
        task1.setStartDate(startDate);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setStartDate(startDate);
        tasks.add(task2);

        // Mock the behavior of taskRepository.findAllByStartDate()
        when(taskRepository.findAllByStartDate(startDate)).thenReturn(Optional.of(tasks));

        // Call the service to get all tasks by the specified start date
        List<Task> foundTasks = taskService.findAllByStartDate(startDate);

        // Verify that the service returns the expected list of tasks
        assertNotNull(foundTasks);
        assertEquals(2, foundTasks.size());
        assertEquals(task1.getId(), foundTasks.get(0).getId());
        assertEquals(task2.getId(), foundTasks.get(1).getId());
    }

    @Test
    public void testFindAllByEndDate() {
        // Define a sample end date
        Date endDate = new Date();

        // Define a list of sample tasks with the specified end date
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setId(1L);
        task1.setEndDate(endDate);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setEndDate(endDate);

        // Mock the behavior of taskRepository.findAllByEndDate()
        when(taskRepository.findAllByEndDate(endDate)).thenReturn(Optional.of(tasks));

        // Call the service to get all tasks by the specified end date
        List<Task> foundTasks = taskService.findAllByEndDate(endDate);

        // Verify that the service returns the expected list of tasks
        assertNotNull(foundTasks);
        assertEquals(2, foundTasks.size());
        assertEquals(task1.getId(), foundTasks.get(0).getId());
        assertEquals(task2.getId(), foundTasks.get(1).getId());
    }

    @Test
    public void testSave() {
        // Define a sample task to be saved
        Task task = new Task();
        task.setId(1L);

        // Mock the behavior of taskRepository.save()
        doNothing().when(taskRepository).save(any(Task.class));

        // Call the service to save the task
        taskService.save(task);

        // Verify that the service calls taskRepository.save with the correct argument
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testDeleteById() {
        // Mock the behavior of taskRepository.deleteById()
        taskService.deleteById(1L);

        // Verify that the service calls deleteById with the correct argument
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAllWithEmptyList() {
        // Mock the behavior of taskRepository.findAll() when the list is empty
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        // Call the service to get all tasks
        List<Task> foundTasks = taskService.findAll();

        // Verify that the service returns an empty list when no tasks are found
        assertNotNull(foundTasks);
        assertTrue(foundTasks.isEmpty());
    }

    @Test
    public void testFindAllByStartDateWithEmptyList() {
        // Define a start date
        Date startDate = new Date();

        // Mock the behavior of taskRepository.findAllByStartDate() when the list is empty
        when(taskRepository.findAllByStartDate(startDate)).thenReturn(Optional.empty());

        // Call the service to get all tasks by start date
        List<Task> foundTasks = taskService.findAllByStartDate(startDate);

        // Verify that the service returns an empty list when no tasks with the specified start date are found
        assertNotNull(foundTasks);
        assertTrue(foundTasks.isEmpty());
    }

    @Test
    public void testFindAllByEndDateWithEmptyList() {
        // Define an end date
        Date endDate = new Date();

        // Mock the behavior of taskRepository.findAllByEndDate() when the list is empty
        when(taskRepository.findAllByEndDate(endDate)).thenReturn(Optional.empty());

        // Call the service to get all tasks by end date
        List<Task> foundTasks = taskService.findAllByEndDate(endDate);

        // Verify that the service returns an empty list when no tasks with the specified end date are found
        assertNotNull(foundTasks);
        assertTrue(foundTasks.isEmpty());
    }
}
