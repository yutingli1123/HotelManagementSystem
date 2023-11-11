package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import ca.mcgill.ecse.hotelmanagementbackend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.time.LocalTime;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByStartTime(LocalTime time) {
        return taskRepository.findAllByStartTime(time).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByEndTime(LocalTime time) {
        return taskRepository.findAllByEndTime(time).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByStartTimeBetween(LocalTime startTime, LocalTime endTime) {
        return taskRepository.findAllByStartTimeBetween(startTime, endTime).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByEndTimeBetween(LocalTime startTime, LocalTime endTime) {
        return taskRepository.findAllByEndTimeBetween(startTime, endTime).orElse(null);
    }

    @Transactional
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}
