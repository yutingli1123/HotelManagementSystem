package ca.mcgill.ecse.hotelmanagementbackend.controller;


import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Request;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse.hotelmanagementbackend.repository.ReservationRepository;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Request;
import ca.mcgill.ecse.hotelmanagementbackend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/Request")

public class RequestController {
    @Autowired
    private RequestService requestService;

    @GetMapping("find-all")
    public List<Request> findAll() {
        return requestService.findAll();
    }

    @GetMapping("add-request")
    public void addRequest(Request request) {
        requestService.addRequest(request);
    }

    @GetMapping("delete-by-Id")
    public void deleteRequestById(Long requestId) {
        requestService.deleteRequestById(requestId);
    }


}

