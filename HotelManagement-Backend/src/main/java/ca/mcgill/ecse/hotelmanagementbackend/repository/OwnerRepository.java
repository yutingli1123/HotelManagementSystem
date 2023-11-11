package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<List<Owner>> findAllByName(String name);
    Optional<Owner> findByUsername(String username);

    Optional<Owner> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteByUsername(String username);
}
