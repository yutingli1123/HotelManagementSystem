package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }


    @Transactional(readOnly = true)
    public List<Reservation> findAllByCheckInDate(Date checkInDate) {
        return reservationRepository.findAllByCheckInDate(checkInDate).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByCheckOutDate(Date checkOutDate) {
        return reservationRepository.findAllByCheckOutDate(checkOutDate).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByCheckInDateAndCheckOutDateRange(Date checkInDate, Date checkOutDate) {
        return reservationRepository.findAllByCheckInDateAfterAndCheckOutDateBefore(checkInDate, checkOutDate).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByCheckInDate(Date checkInDate) {
        reservationRepository.deleteAllByCheckInDate(checkInDate);
    }

    @Transactional
    public void deleteAllByCheckOutDate(Date checkOutDate) {
        reservationRepository.deleteAllByCheckOutDate(checkOutDate);
    }

    @Transactional
    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
