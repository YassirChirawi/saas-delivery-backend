package com.saas.delivery.model;

import lombok.Data;

@Data
public class PartnerRequest {
    private String id;
    private String ownerName;
    private String restaurantName;
    private String email;
    private String phone;
    private String description;
    private String address;
    private String status; // PENDING, APPROVED, REJECTED
}