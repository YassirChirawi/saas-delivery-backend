package com.saas.delivery.controller;

import com.saas.delivery.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders") // 👈 L'URL doit correspondre
public class OrderController {
    private final com.saas.delivery.service.OrderService service;

    public OrderController(com.saas.delivery.service.OrderService service) {
        this.service = service;
    }

    @PostMapping
    public String createOrder(@RequestBody Order order) {
        return service.createOrder(order);
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{id}/status")
    public void updateStatus(@org.springframework.web.bind.annotation.PathVariable String id,
            @org.springframework.web.bind.annotation.RequestParam String status) {
        service.updateOrderStatus(id, status);

    }
}
