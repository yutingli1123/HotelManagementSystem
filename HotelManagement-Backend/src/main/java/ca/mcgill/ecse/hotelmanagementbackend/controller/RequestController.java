package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Request;
import ca.mcgill.ecse.hotelmanagementbackend.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("add-request")
    public void addRequest(Request request) {
        requestService.addRequest(request);
    }

    @DeleteMapping("delete-by-Id")
    public void deleteRequestById(Long requestId) {
        requestService.deleteRequestById(requestId);
    }


}

