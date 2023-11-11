package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<List<Customer>> findAllByName(String name);
    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteByUsername(String username);
}
