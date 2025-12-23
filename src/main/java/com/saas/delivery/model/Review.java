package com.saas.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private String id;
    private String restaurantId;
    private String orderId;
    private String userId;
    private String clientName;
    private Double rating;
    private String comment;
    private String createdAt;
}
