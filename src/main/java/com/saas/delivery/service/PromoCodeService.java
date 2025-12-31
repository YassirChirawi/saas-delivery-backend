package com.saas.delivery.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.saas.delivery.model.PromoCode;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class PromoCodeService {

    private static final String COLLECTION_NAME = "promo_codes";

    public void createPromoCode(PromoCode promoCode) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        promoCode.setCurrentUsage(0);
        promoCode.setActive(true);
        // Use the code itself as the document ID
        db.collection(COLLECTION_NAME).document(promoCode.getCode()).set(promoCode).get();
    }

    public PromoCode getPromoCode(String code) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        var snapshot = db.collection(COLLECTION_NAME).document(code).get().get();
        if (snapshot.exists()) {
            return snapshot.toObject(PromoCode.class);
        }
        return null; // or throw exception
    }

    /**
     * Valide le code et retourne le montant de la réduction.
     */
    public double calculateDiscount(String code, double orderTotal, String restaurantId) throws Exception {
        PromoCode promo = getPromoCode(code);

        if (promo == null || !promo.isActive()) {
            throw new Exception("Code promo invalide ou inactif");
        }

        // Validate Restaurant Specificity
        if (promo.getRestaurantId() != null && !promo.getRestaurantId().isEmpty()) {
            if (restaurantId == null || !promo.getRestaurantId().equals(restaurantId)) {
                throw new Exception("Ce code promo n'est pas valide pour ce restaurant");
            }
        }

        if (promo.getExpiryDate() != null && promo.getExpiryDate().before(new Date())) {
            throw new Exception("Code promo expiré");
        }

        if (promo.getMaxUsage() > 0 && promo.getCurrentUsage() >= promo.getMaxUsage()) {
            throw new Exception("Nombre maximum d'utilisations atteint pour ce code");
        }

        double discount = 0;
        if ("PERCENTAGE".equalsIgnoreCase(promo.getDiscountType())) {
            discount = orderTotal * (promo.getValue() / 100.0);
        } else if ("FIXED".equalsIgnoreCase(promo.getDiscountType())) {
            discount = promo.getValue();
        }

        // Discount cannot exceed total
        if (discount > orderTotal) {
            discount = orderTotal;
        }

        return discount;
    }

    public void incrementUsage(String code) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            // Simple increment, in a real app use transaction for safety
            PromoCode promo = getPromoCode(code);
            if (promo != null) {
                db.collection(COLLECTION_NAME).document(code).update("currentUsage", promo.getCurrentUsage() + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public java.util.List<PromoCode> getPromoCodesByRestaurant(String restaurantId)
            throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        var query = db.collection(COLLECTION_NAME).whereEqualTo("restaurantId", restaurantId).get().get();
        return query.toObjects(PromoCode.class);
    }

    public void deletePromoCode(String code) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME).document(code).delete();
    }
}
