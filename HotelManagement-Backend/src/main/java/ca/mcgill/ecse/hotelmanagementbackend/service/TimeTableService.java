package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import ca.mcgill.ecse.hotelmanagementbackend.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimeTableService {
    @Autowired
    TimeTableRepository timeTableRepository;

    @Transactional(readOnly = true)
    public List<TimeTable> findAll() {
        return timeTableRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TimeTable findById(Long id) {
        return timeTableRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(TimeTable timeTable) {
        timeTableRepository.save(timeTable);
    }

    @Transactional
    public void deleteById(Long id) {
        timeTableRepository.deleteById(id);
    }
}
