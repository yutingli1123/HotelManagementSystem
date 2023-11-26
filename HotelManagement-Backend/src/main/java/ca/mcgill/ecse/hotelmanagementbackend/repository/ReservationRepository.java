package ca.mcgill.ecse.hotelmanagementbackend.repository;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<List<Reservation>> findAllByCheckInDate(Date checkInDate);

    Optional<List<Reservation>> findAllByCheckOutDate(Date checkInDate);

    Optional<List<Reservation>> findAllByCheckInDateAfterAndCheckOutDateBefore(Date startDate, Date endDate);

    void deleteAllByCheckInDate(Date checkInDate);
    void deleteAllByCheckOutDate(Date checkOutDate);
}
