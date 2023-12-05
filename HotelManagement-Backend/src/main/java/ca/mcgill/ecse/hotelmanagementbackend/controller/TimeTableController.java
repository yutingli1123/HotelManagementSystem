package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.TimeTableDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TaskService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/timeTables")
@Slf4j
public class TimeTableController {
    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<TimeTableDto> getAllTimeTables() {
        List<TimeTable> timeTables = timeTableService.findAll();
        List<TimeTableDto> timeTableDtos = new ArrayList<>();
        timeTables.forEach(timeTable -> {
            List<Employee> employees = timeTable.getEmployees();
            List<Long> employeeIds = new ArrayList<>();
            employees.forEach(employee -> employeeIds.add(employee.getId()));
            List<Task> tasks = timeTable.getTasks();
            List<Long> taskIds = new ArrayList<>();
            tasks.forEach(task -> taskIds.add(task.getId()));
            timeTableDtos.add(new TimeTableDto(timeTable.getId(), timeTable.getTimeTableName(), taskIds, employeeIds));
        });
        return timeTableDtos;
    }

    @GetMapping("/by-id/{id}")
    public TimeTable getTimeTable(@PathVariable Long id) {
        return timeTableService.findById(id);
    }

    @PostMapping
    public Long saveTimeTable(@Valid @RequestBody TimeTable timeTable) {
        timeTableService.save(timeTable);
        return timeTable.getId();
    }

    @PostMapping("/add")
    public Long addTimeTable(@Valid @RequestBody TimeTableDto timeTableDto) {
        TimeTable timeTable = new TimeTable(timeTableDto.getTimeTableName());
        timeTableService.save(timeTable);
        return timeTable.getId();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateTimeTable(@Valid @RequestBody TimeTableDto timeTableDto) {
        TimeTable timeTable = timeTableService.findById(timeTableDto.getId());
        if (timeTable != null) {
            List<Employee> employees = timeTable.getEmployees();
            employees.forEach(employee -> {
                employee.setTimeTable(null);
                employeeService.save(employee);
            });
            List<Task> tasks = timeTable.getTasks();
            tasks.forEach(task -> {
                task.setTimeTable(null);
                taskService.save(task);
            });
            List<Long> employeeIds = timeTableDto.getEmployeeIds();
            List<Employee> employeesFromIds = new ArrayList<>();
            employeeIds.forEach(id -> employeesFromIds.add(employeeService.findById(id)));
            List<Long> taskIds = timeTableDto.getTaskIds();
            List<Task> tasksFromIds = new ArrayList<>();
            taskIds.forEach(id -> tasksFromIds.add(taskService.findById(id)));
            timeTable.setTimeTableName(timeTableDto.getTimeTableName());
            timeTable.setEmployees(employeesFromIds);
            timeTable.setTasks(tasksFromIds);
            timeTableService.save(timeTable);
            employeesFromIds.forEach(employee -> {
                employee.setTimeTable(timeTable);
                employeeService.save(employee);
            });
            tasksFromIds.forEach(task -> {
                task.setTimeTable(timeTable);
                taskService.save(task);
            });
            return ResponseEntity.ok(Boolean.TRUE);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.findById(id);
        List<Employee> employees = timeTable.getEmployees();
        List<Task> tasks = timeTable.getTasks();
        tasks.forEach(task -> {
            task.setTimeTable(null);
            taskService.save(task);
        });
        employees.forEach(employee -> {
            employee.setTimeTable(null);
            employeeService.save(employee);
        });
        timeTableService.deleteById(id);
    }
}
