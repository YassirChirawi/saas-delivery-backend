package com.saas.delivery.model;

import lombok.Data;

@Data
public class OrderItem {

    // 1. LE LIEN (Pour les stats et retrouver le produit original si besoin)
    private String productId;

    // 2. LE SNAPSHOT (Copie obligatoire pour l'historique)
    private String name;
    private double price;     // Le prix AU MOMENT de la commande (ex: 10€)

    // 3. LA QUANTITÉ
    private int quantity;
}
