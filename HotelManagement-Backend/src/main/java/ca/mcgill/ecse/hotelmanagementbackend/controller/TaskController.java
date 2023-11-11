package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/by-date/start/{startDate}")
    public List<Task> getAllTasksByStartDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate) {
        return taskService.findAllByStartDate(startDate);
    }

    @GetMapping("/by-date/end/{endDate}")
    public List<Task> getAllTasksByEndDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return taskService.findAllByEndDate(endDate);
    }

    @GetMapping("/by-id/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public void saveTask(@Valid @RequestBody Task task) {
        taskService.save(task);
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
    }
}

