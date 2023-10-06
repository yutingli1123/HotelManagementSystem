package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.TaskService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class TaskTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TimeTableService timeTableService;

    @Test
    @Transactional
    void testSaveTask() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 2 * 3600 * 1000);
        Task task = new Task(startDate, endDate,"Test", "Test");
        taskService.save(task);
        assertEquals("Test saving task",1,taskService.findAll().size());
    }

    @Test
    @Transactional
    void testSearchTaskById() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 2 * 3600 * 1000);
        Task task = new Task(startDate, endDate,"Test", "Test");
        taskService.save(task);
        assertEquals("Test searching task by ID",task,taskService.findById(task.getId()));
    }

    @Test
    @Transactional
    void testSearchByStartDate() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 2 * 3600 * 1000);
        Task task = new Task(startDate, endDate,"Test", "Test");
        taskService.save(task);
        assertTrue("Test searching by start date",taskService.findAllByStartDate(startDate).contains(task));
    }

    @Test
    @Transactional
    void testSearchByEndDate() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 2 * 3600 * 1000);
        Task task = new Task(startDate, endDate,"Test", "Test");
        taskService.save(task);
        assertTrue("Test searching by end date",taskService.findAllByEndDate(endDate).contains(task));
    }

    @Test
    @Transactional
    void testReferenceWithTimeTable() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 2 * 3600 * 1000);
        Task task = new Task(startDate, endDate,"Test", "Test");
        TimeTable timeTable = new TimeTable("TestTable");
        task.setTimeTable(timeTable);
        List<Task> taskList = timeTable.getTasks();
        taskList.add(task);
        timeTable.setTasks(taskList);
        timeTableService.save(timeTable);
        taskService.save(task);
        Task taskFromDB = taskService.findById(task.getId());
        assertEquals("Test reference with time table",taskFromDB.getTimeTable(),timeTable);
    }
}
