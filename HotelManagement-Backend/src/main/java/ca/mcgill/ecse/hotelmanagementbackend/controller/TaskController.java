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
@RequestMapping("/api/v1")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/tasksByStartDate")
    public List<Task> getAllTasksByStartDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate) {
        return taskService.findAllByStartDate(startDate);
    }

    @GetMapping("/tasksByEndDate")
    public List<Task> getAllTasksByEndDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return taskService.findAllByEndDate(endDate);
    }

    @GetMapping("/task")
    public Task getTask(@RequestParam Long id) {
        return taskService.findById(id);
    }

    @PostMapping("/task")
    public void saveTask(@Valid @RequestBody Task task) {
        taskService.save(task);
    }

    @DeleteMapping("/task")
    public void deleteTask(@RequestParam Long id) {
        taskService.deleteById(id);
    }
}

