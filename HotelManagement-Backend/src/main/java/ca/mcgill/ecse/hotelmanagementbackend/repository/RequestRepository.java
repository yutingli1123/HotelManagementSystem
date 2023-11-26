package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}