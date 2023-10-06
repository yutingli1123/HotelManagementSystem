package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    HotelRepository hotelRepository;

    @Transactional(readOnly = true)
    public Hotel findById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Transactional
    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Transactional
    public void deleteById(Long id) {
        hotelRepository.deleteById(id);
    }
}
