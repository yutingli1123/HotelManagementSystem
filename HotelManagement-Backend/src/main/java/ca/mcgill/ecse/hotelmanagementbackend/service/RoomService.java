package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import ca.mcgill.ecse.hotelmanagementbackend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Room> findAllByRoomType(RoomType type) {
        return roomRepository.findAllByType(type).orElse(null);
    }

    @Transactional(readOnly = true)
    public Room findById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Transactional
    public void saveAll(Room room) {
        roomRepository.save(room);
    }

    @Transactional
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }
}
