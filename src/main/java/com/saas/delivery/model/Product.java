package com.saas.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;          // L'ID unique généré par Firebase
    private String name;        // Ex: "Burger Double"
    private String description; // Ex: "Sauce maison, cheddar..."
    private Double price;       // Ex: 12.50
    private String imageUrl;    // URL de l'image (plus tard)
    private String category;    // Ex: "BURGERS", "BOISSONS"
    private String restaurantId;// Pour savoir à qui appartient ce plat
}