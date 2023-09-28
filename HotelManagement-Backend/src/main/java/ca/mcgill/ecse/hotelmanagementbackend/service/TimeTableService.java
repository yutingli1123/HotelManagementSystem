package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeTableService {
    @Autowired
    TimeTableRepository timeTableRepository;

    public List<TimeTable> findAll() {
        return timeTableRepository.findAll();
    }

    public TimeTable findById(Long id) {
        return timeTableRepository.findById(id).orElse(null);
    }

    public void save(TimeTable timeTable) {
        timeTableRepository.save(timeTable);
    }

    public void deleteById(Long id) {
        timeTableRepository.deleteById(id);
    }
}
