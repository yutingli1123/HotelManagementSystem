package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.TaskDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.TaskService;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TimeTableService timeTableService;

    @GetMapping
    public List<TaskDto> getAllTasks() {
        List<Task> taskList = taskService.findAll();
        List<TaskDto> taskDtos = new ArrayList<>();
        taskList.forEach(task -> taskDtos.add(new TaskDto(task.getId(),task.getStartTime(),task.getEndTime(),task.getDayOfTheWeek(),task.getTaskName(),task.getTaskDescription())));
        return taskDtos;
    }

    @GetMapping("/ids")
    public List<Long> getAllTaskIds() {
        List<Task> taskList = taskService.findAll();
        List<Long> taskIds = new ArrayList<>();
        taskList.forEach(task -> taskIds.add(task.getId()));
        return taskIds;
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

    @PostMapping("/add")
    public Long addeTask(@Valid @RequestBody TaskDto taskDto) {
        Task task = new Task(taskDto.getStartTime(),taskDto.getEndTime(),taskDto.getDayOfTheWeek(),taskDto.getTaskName(),taskDto.getTaskDescription());
        taskService.save(task);
        return task.getId();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateTask(@Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.findById(taskDto.getId());
        if (task != null) {
            TimeTable timeTable = task.getTimeTable();
            if  (timeTable != null) {
                List<Task> tasks = timeTable.getTasks();
                tasks.remove(task);
                task.setStartTime(taskDto.getStartTime());
                task.setEndTime(taskDto.getEndTime());
                task.setDayOfTheWeek(taskDto.getDayOfTheWeek());
                task.setTaskName(taskDto.getTaskName());
                task.setTaskDescription(taskDto.getTaskDescription());
                taskService.save(task);
                tasks.add(task);
                timeTable.setTasks(tasks);
                timeTableService.save(timeTable);
                return ResponseEntity.ok(Boolean.TRUE);
            } else {
                task.setStartTime(taskDto.getStartTime());
                task.setEndTime(taskDto.getEndTime());
                task.setDayOfTheWeek(taskDto.getDayOfTheWeek());
                task.setTaskName(taskDto.getTaskName());
                task.setTaskDescription(taskDto.getTaskDescription());
                taskService.save(task);
                return ResponseEntity.ok(Boolean.TRUE);
            }

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
    }
}

