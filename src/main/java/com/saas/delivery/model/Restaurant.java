package com.saas.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private String id;
    private String name;
    private String ownerName;
    private String whatsappPhone;
    private String email;

    // 👇 CHANGEMENT ICI : On renomme "isActive" en "active"
    private boolean active;

    private Double rating = 0.0;
    private Integer ratingCount = 0;

    private String imageUrl;
    private String description;
    private String address;

    // Delivery Info
    private String deliveryTime; // e.g. "20-30 min"
    private Double deliveryFees; // e.g. 15.0 (DH)
}