package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
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

    @GetMapping("/by-time/start")
    public List<Task> getAllTasksByStartDate(@RequestParam LocalTime startTime) {
        return taskService.findAllByStartTime(startTime);
    }

    @GetMapping("/by-time/end")
    public List<Task> getAllTasksByEndDate(@RequestParam LocalTime endTime) {
        return taskService.findAllByEndTime(endTime);
    }

    @GetMapping("/by-time/start/range")
    public List<Task> getAllTasksByStartDateBetween(@RequestParam LocalTime startTime, @RequestParam LocalTime endTime) {
        return taskService.findAllByStartTimeBetween(startTime, endTime);
    }

    @GetMapping("/by-time/end/range")
    public List<Task> getAllTasksByEndDateBetween(@RequestParam LocalTime startTime, @RequestParam LocalTime endTime) {
        return taskService.findAllByEndTimeBetween(startTime, endTime);
    }

    @GetMapping("/by-id/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public Long saveTask(@Valid @RequestBody Task task) {
        taskService.save(task);
        return task.getId();
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
    }
}

