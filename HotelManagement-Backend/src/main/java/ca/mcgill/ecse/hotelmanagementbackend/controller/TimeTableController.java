package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/timeTables")
public class TimeTableController {
    @Autowired
    private TimeTableService timeTableService;

    @GetMapping
    public List<TimeTable> getAllTimeTables() {
        return timeTableService.findAll();
    }

    @GetMapping("/by-id/{id}")
    public TimeTable getTimeTable(@PathVariable Long id) {
        return timeTableService.findById(id);
    }

    @PostMapping
    public void saveTimeTable(@Valid @RequestBody TimeTable timeTable) {
        timeTableService.save(timeTable);
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteTimeTable(@PathVariable Long id) {
        timeTableService.deleteById(id);
    }
}
