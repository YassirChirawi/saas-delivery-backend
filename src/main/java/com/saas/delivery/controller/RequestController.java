package com.saas.delivery.controller;

// 1. Imports de tes classes
import com.saas.delivery.model.PartnerRequest;
import com.saas.delivery.service.RequestService;

// 2. Imports Spring Web (Pour les annotations @RestController, @GetMapping...)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// (L'étoile * importe RestController, RequestMapping, CrossOrigin, PathVariable, RequestBody, etc.)

// 3. Imports Java Standard
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/requests")
@CrossOrigin(origins = "http://localhost:4200")
public class RequestController {

    @Autowired
    private RequestService service;

    @PostMapping
    public String createRequest(@RequestBody PartnerRequest request) throws ExecutionException, InterruptedException {
        return service.createRequest(request);
    }

    @GetMapping
    public List<PartnerRequest> getAllRequests() throws ExecutionException, InterruptedException {
        return service.getAllRequests();
    }

    @DeleteMapping("/{id}")
    public String deleteRequest(@PathVariable String id) {
        return service.deleteRequest(id);
    }
}