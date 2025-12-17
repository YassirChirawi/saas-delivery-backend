package com.saas.delivery.model;

import lombok.Data;
import java.util.List;
import java.util.Date;

@Data
public class Order {
    private String uid;          // L'ID unique de la commande (Firestore)
    private String restaurantId;
    private String restaurantName;

    // Infos Client (Soit User ID, soit infos Guest)
    private String customerId;
    private GuestInfo guestInfo; // Voir classe plus bas

    private List<OrderItem> items; // La liste des plats

    private double totalPrice;
    private String deliveryType; // "pickup" ou "delivery"
    private String deliveryZone;
    private String note;         // La remarque du client

    private String status;       // "PENDING", "ACCEPTED", "READY", "DELIVERED"
    private Date createdAt;      // Date de la commande
}
