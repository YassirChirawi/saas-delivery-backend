package com.saas.delivery.controller;

import com.saas.delivery.model.Product;
import com.saas.delivery.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/products") // Toutes les routes commenceront par ça
public class ProductController {

    private final ProductService productService;

    // Injection de dépendance (Spring connecte le Service au Controller)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Route pour AJOUTER (POST)
    @PostMapping
    public String addProduct(@RequestBody Product product) throws ExecutionException, InterruptedException {
        return productService.createProduct(product);
    }

    // Route pour LIRE (GET)
    @GetMapping
    public List<Product> getProducts() throws ExecutionException, InterruptedException {
        return productService.getAllProducts();
    }
}