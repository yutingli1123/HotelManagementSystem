package ca.mcgill.ecse.hotelmanagementbackend.service;


import ca.mcgill.ecse.hotelmanagementbackend.entity.Request;
import ca.mcgill.ecse.hotelmanagementbackend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;

    @Transactional(readOnly = true)
    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    @Transactional
    public void addRequest(Request request) {
        requestRepository.save(request);
    }

    @Transactional
    public void deleteRequestById(Long requestId) {
        requestRepository.deleteById(requestId);
    }
}