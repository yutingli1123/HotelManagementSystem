package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
