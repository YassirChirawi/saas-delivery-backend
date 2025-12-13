package com.saas.delivery.controller;

import com.saas.delivery.model.Restaurant;
import com.saas.delivery.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/restaurants")
@CrossOrigin(origins = "http://localhost:4200")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping
    public String create(@RequestBody Restaurant restaurant) throws ExecutionException, InterruptedException {
        return service.createRestaurant(restaurant);
    }

    @GetMapping
    public List<Restaurant> getAll() throws ExecutionException, InterruptedException {
        // 👇 C'était ici l'erreur, il manquait "Restaurants" à la fin
        return service.getAllRestaurants();
    }

    @PutMapping("/{id}/activate")
    public void activate(@PathVariable String id, @RequestParam boolean status) {
        service.activateRestaurant(id, status);
    }

    @GetMapping("/{id}")
    public Restaurant getById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return service.getRestaurantById(id);
    }

    @GetMapping("/owner/{email}")
    public Restaurant getByEmail(@PathVariable String email) throws ExecutionException, InterruptedException {
        return service.getRestaurantByEmail(email);
    }
}