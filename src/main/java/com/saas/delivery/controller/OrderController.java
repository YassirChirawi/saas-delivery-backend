package com.saas.delivery.controller;

import com.saas.delivery.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders") // 👈 L'URL doit correspondre
public class OrderController {
    @PostMapping
    public String createOrder(@RequestBody Order order) {
        // ... logique de sauvegarde ...
        return "ID_COMMANDE";
    }
}
