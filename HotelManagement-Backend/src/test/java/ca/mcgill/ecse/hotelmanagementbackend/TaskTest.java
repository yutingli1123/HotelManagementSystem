package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.TaskService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class TaskTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TimeTableService timeTableService;

    @Test
    @Transactional
    void testSaveTask() {
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(2);
        Task task = new Task(startTime, endTime, "Monday", "Test", "Test");
        taskService.save(task);
        assertEquals("Test saving task", 1, taskService.findAll().size());
    }

    @Test
    @Transactional
    void testSearchTaskById() {
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(2);
        Task task = new Task(startTime, endTime, "Monday", "Test", "Test");
        taskService.save(task);
        assertEquals("Test searching task by ID", task, taskService.findById(task.getId()));
    }

    @Test
    @Transactional
    void testSearchByStartDate() {
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(2);
        Task task = new Task(startTime, endTime, "Monday", "Test", "Test");
        taskService.save(task);
        assertTrue("Test searching by start date", taskService.findAllByStartTime(startTime).contains(task));
    }

    @Test
    @Transactional
    void testSearchByEndDate() {
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(2);
        Task task = new Task(startTime, endTime, "Monday", "Test", "Test");
        taskService.save(task);
        assertTrue("Test searching by end date", taskService.findAllByEndTime(endTime).contains(task));
    }

    @Test
    @Transactional
    void testReferenceWithTimeTable() {
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(2);
        Task task = new Task(startTime, endTime, "Monday", "Test", "Test");
        TimeTable timeTable = new TimeTable("TestTable");
        task.setTimeTable(timeTable);
        List<Task> taskList = timeTable.getTasks();
        taskList.add(task);
        timeTable.setTasks(taskList);
        timeTableService.save(timeTable);
        taskService.save(task);
        Task taskFromDB = taskService.findById(task.getId());
        assertEquals("Test reference with time table", taskFromDB.getTimeTable(), timeTable);
    }
}
