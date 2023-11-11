package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Owner> findAllByName(String name) {
        return ownerRepository.findAllByName(name).orElse(null);
    }

    @Transactional(readOnly = true)
    public Owner findById(Long id) {
        return ownerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Owner findByUsername(String username) {
        return ownerRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public Owner findByEmail(String email) {
        return ownerRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void save(Owner owner) {
        ownerRepository.save(owner);
    }

    @Transactional
    public void deleteById(Long id) {
        ownerRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUsername(String username) {
        ownerRepository.deleteByUsername(username);
    }

    @Transactional
    public void deleteByEmail(String email) {
        ownerRepository.deleteByEmail(email);
    }
}
