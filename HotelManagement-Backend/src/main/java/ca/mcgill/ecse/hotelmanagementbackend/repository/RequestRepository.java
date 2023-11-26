package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    void addRequest(String title, Room room, String content);

    void deleteRequestById(Long requestId);

    void distributeTask(Long requestId, Employee employee);
}