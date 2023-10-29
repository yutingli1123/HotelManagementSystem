package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.service.TimeTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class TimeTableController {
    @Autowired
    private TimeTableService timeTableService;

    @GetMapping("/timeTables")
    public List<TimeTable> getAllTimeTables() {
        return timeTableService.findAll();
    }

    @GetMapping("/timeTable")
    public TimeTable getTimeTable(@RequestParam Long id) {
        return timeTableService.findById(id);
    }

    @PostMapping("/timeTable")
    public void saveTimeTable(@Valid @RequestBody TimeTable timeTable) {
        timeTableService.save(timeTable);
    }

    @DeleteMapping("/timeTable")
    public void deleteTimeTable(@RequestParam Long id) {
        timeTableService.deleteById(id);
    }
}
