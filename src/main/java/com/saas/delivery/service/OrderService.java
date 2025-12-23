package com.saas.delivery.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.saas.delivery.model.Order;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    private final NotificationService notificationService;

    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public String createOrder(Order order) {
        Firestore db = FirestoreClient.getFirestore();

        // 1. Générer un ID unique pour la commande
        String orderId = UUID.randomUUID().toString();

        // 2. Remplir les champs automatiques
        order.setUid(orderId);
        order.setCreatedAt(new Date()); // Date du serveur (très important)
        order.setStatus("PENDING"); // Par défaut : En attente

        // 3. Sauvegarder dans la collection "orders"
        try {
            db.collection("orders").document(orderId).set(order);
            return orderId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la commande");
        }
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            // 1. Update status
            db.collection("orders").document(orderId).update("status", newStatus).get();

            // 2. Fetch Order to get user info
            Order order = db.collection("orders").document(orderId).get().get().toObject(Order.class);

            if (order != null && order.getCustomerId() != null) {
                // 3. Fetch User (Assuming 'users' collection exists and has 'fcmToken')
                // Note: If you don't have a User model class yet, we can use a generic Map or
                // DocumentSnapshot
                var userDoc = db.collection("users").document(order.getCustomerId()).get().get();
                if (userDoc.exists()) {
                    String fcmToken = userDoc.getString("fcmToken");
                    if (fcmToken != null) {
                        String title = "Update on your Order";
                        String body = "Your order status is now: " + newStatus;
                        notificationService.sendNotification(fcmToken, title, body);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update order status");
        }
    }
}
