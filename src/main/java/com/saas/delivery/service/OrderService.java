package com.saas.delivery.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.saas.delivery.model.Order;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    public String createOrder(Order order) {
        Firestore db = FirestoreClient.getFirestore();

        // 1. Générer un ID unique pour la commande
        String orderId = UUID.randomUUID().toString();

        // 2. Remplir les champs automatiques
        order.setUid(orderId);
        order.setCreatedAt(new Date()); // Date du serveur (très important)
        order.setStatus("PENDING");     // Par défaut : En attente

        // 3. Sauvegarder dans la collection "orders"
        try {
            db.collection("orders").document(orderId).set(order);
            return orderId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la commande");
        }
    }
}
