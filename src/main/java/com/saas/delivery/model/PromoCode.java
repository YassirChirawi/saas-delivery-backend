package com.saas.delivery.model;

import lombok.Data;
import java.util.Date;

@Data
public class PromoCode {
    private String code; // The code itself (e.g., "WELCOME2024") - used as Firestore ID
    private String discountType; // "PERCENTAGE" or "FIXED"
    private double value; // e.g., 20 (for 20% or 20€)
    private Date expiryDate;
    private int maxUsage;
    private int currentUsage;
    private boolean active;
    private String restaurantId; // Optional: Link to a specific restaurant
}
