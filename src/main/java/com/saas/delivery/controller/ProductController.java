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
    @GetMapping("/restaurant/{restaurantId}")
    public List<Product> getByRestaurant(@PathVariable String restaurantId) throws ExecutionException, InterruptedException {
        // 👇 AJOUTE CES LIGNES DE DEBUG
        System.out.println("🔍 REQUÊTE RECUE - ID demandé : " + restaurantId);

        List<Product> products = productService.getProductsByRestaurant(restaurantId);

        System.out.println("✅ RESULTAT - Nombre de produits trouvés : " + products.size());
        return products;
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable String id, @RequestBody Product product) throws ExecutionException, InterruptedException {
        // Attention : Firebase update ne renvoie pas l'objet, juste un timestamp
        // Assure-toi que ton ProductService a une méthode update
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) throws ExecutionException, InterruptedException {
        return productService.deleteProduct(id);
    }
}