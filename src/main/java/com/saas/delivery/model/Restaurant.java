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

    private String imageUrl;
    private String description;
    private String address;
}