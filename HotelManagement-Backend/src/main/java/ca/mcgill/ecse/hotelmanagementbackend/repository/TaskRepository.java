package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findAllByStartTime(LocalTime startTime);
    Optional<List<Task>> findAllByEndTime(LocalTime endTime);

    Optional<List<Task>> findAllByStartTimeBetween(LocalTime startTime, LocalTime endTime);

    Optional<List<Task>> findAllByEndTimeBetween(LocalTime startTime, LocalTime endTime);


}
