package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.*;
import ca.mcgill.ecse.hotelmanagementbackend.service.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
@SpringBootTest
public class TimeTableTest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TimeTableService timeTableService;

    @Test
    @Transactional
    void testReadAndWriteTimeTableObjects() {
        TimeTable timeTable = new TimeTable("timeTable1");
        timeTableService.save(timeTable);
        assertEquals("checks if TimeTable object is saved and if can be read", 1, timeTableService.findAll().size());
    }

    @Test
    @Transactional
    void testReadAndWriteRoomAttributes(){
        TimeTable timeTable = new TimeTable("timeTable1");
        timeTableService.save(timeTable);
        //test read
        assertEquals("checks if TimeTable attribute can be read", "timeTable1", timeTable.getTimeTableName());
        // test write
        timeTable.setTimeTableName("newTimeTable");
        timeTableService.save(timeTable);
        assertEquals("checks if TimeTable attribute can be read", "newTimeTable", timeTable.getTimeTableName());
    }

    @Test
    @Transactional
    void testReadAndWriteReference(){
        List<Task> taskList = new ArrayList<>();
        Task task1 = new Task();
        taskService.save(task1);
        Task task2 = new Task();
        taskService.save(task2);
        taskList.add(task1);
        taskList.add(task2);
        TimeTable timeTable = new TimeTable("timeTable1");
        timeTable.setTasks(taskList);
        timeTableService.save(timeTable);
        assertEquals("checks if TimeTable can read and write its attribute which is an instance of Task class", taskList, timeTable.getTasks());
    }
}
