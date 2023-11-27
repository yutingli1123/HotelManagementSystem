package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.TimeTableDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.EmployeeService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TaskService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/timeTables")
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
            timeTableDtos.add(new TimeTableDto(timeTable.getId(), timeTable.getTimeTableName(), employeeIds));
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

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateTimeTable(@Valid @RequestBody TimeTableDto timeTableDto) {
        TimeTable timeTable = timeTableService.findById(timeTableDto.getId());
        if (timeTable != null) {
            List<Employee> employees = timeTable.getEmployees();
            employees.forEach(employee -> {
                employee.setTimeTable(null);
                employeeService.save(employee);
            });
            List<Long> employeeIds = timeTableDto.getEmployeeIds();
            List<Employee> employeesFromIds = new ArrayList<>();
            employeeIds.forEach(id -> employeesFromIds.add(employeeService.findById(id)));
            timeTable.setTimeTableName(timeTableDto.getTimeTableName());
            timeTable.setEmployees(employees);
            timeTableService.save(timeTable);
            employeesFromIds.forEach(employee -> {
                employee.setTimeTable(timeTable);
                employeeService.save(employee);
            });
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteTimeTable(@PathVariable Long id) {
        TimeTable timeTable = timeTableService.findById(id);
        List<Task> tasks = timeTable.getTasks();
        tasks.forEach(task -> {
            task.setTimeTable(null);
            taskService.save(task);
        });
        timeTableService.deleteById(id);
    }
}
