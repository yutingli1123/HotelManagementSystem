package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findAllByName(String name);
    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteByUsername(String username);
}
