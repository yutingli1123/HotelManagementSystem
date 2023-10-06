package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findAllByStartDate(Date startDate);
    Optional<List<Task>> findAllByEndDate(Date endDate);

}
