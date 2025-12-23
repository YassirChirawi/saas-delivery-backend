package com.saas.delivery.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.saas.delivery.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RestaurantService {

    private static final String COLLECTION = "restaurants";
    private static final String REVIEWS_COLLECTION = "reviews";

    public String addReview(String restaurantId, com.saas.delivery.model.Review review)
            throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // 1. Check if order has already been reviewed
        Query query = db.collection(REVIEWS_COLLECTION).whereEqualTo("orderId", review.getOrderId());
        if (!query.get().get().isEmpty()) {
            throw new IllegalArgumentException("This order has already been reviewed.");
        }

        DocumentReference restaurantRef = db.collection(COLLECTION).document(restaurantId);
        DocumentReference reviewRef = db.collection(REVIEWS_COLLECTION).document(); // Auto ID

        review.setId(reviewRef.getId());
        review.setRestaurantId(restaurantId);
        review.setCreatedAt(java.time.Instant.now().toString());

        // Run transaction
        return db.runTransaction(transaction -> {
            DocumentSnapshot restaurantSnapshot = transaction.get(restaurantRef).get();
            Restaurant restaurant = restaurantSnapshot.toObject(Restaurant.class);

            if (restaurant == null) {
                throw new IllegalArgumentException("Restaurant not found");
            }

            // Calculate new rating
            double currentRating = restaurant.getRating() != null ? restaurant.getRating() : 0.0;
            int currentCount = restaurant.getRatingCount() != null ? restaurant.getRatingCount() : 0;

            double newRating = ((currentRating * currentCount) + review.getRating()) / (currentCount + 1);

            // Round to 1 decimal place if needed, or keep precision.

            transaction.update(restaurantRef, "rating", newRating);
            transaction.update(restaurantRef, "ratingCount", currentCount + 1);

            transaction.set(reviewRef, review);

            return review.getId();
        }).get();
    }

    public String createRestaurant(Restaurant restaurant) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // 👇 C'EST ICI LA CORRECTION
        // Au lieu de faire .document(restaurant.getId()) qui plante car getId() est
        // null...

        // 1. On crée une référence vide pour générer un ID unique automatiquement
        DocumentReference docRef = db.collection("restaurants").document();

        // 2. On récupère cet ID généré et on le met dans l'objet Java
        restaurant.setId(docRef.getId());

        // 3. On sauvegarde l'objet complet (qui contient maintenant son ID)
        ApiFuture<WriteResult> result = docRef.set(restaurant);

        return result.get().getUpdateTime().toString();
    }

    public List<Restaurant> getAllRestaurants() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Restaurant> list = new ArrayList<>();
        for (DocumentSnapshot doc : documents) {
            list.add(doc.toObject(Restaurant.class));
        }
        return list;
    }

    // Pour valider un resto
    public void activateRestaurant(String id, boolean status) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("restaurants").document(id).update("active", status);
    }

    public Restaurant getRestaurantById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot doc = db.collection(COLLECTION).document(id).get().get();

        if (doc.exists()) {
            return doc.toObject(Restaurant.class);
        } else {
            return null;
        }
    }

    public Restaurant getRestaurantByEmail(String email) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // On cherche le restaurant qui a cet email
        Query query = db.collection("restaurants").whereEqualTo("email", email);
        List<QueryDocumentSnapshot> docs = query.get().get().getDocuments();

        if (!docs.isEmpty()) {
            Restaurant resto = docs.get(0).toObject(Restaurant.class);
            resto.setId(docs.get(0).getId()); // Important de remettre l'ID
            return resto;
        }
        return null; // Pas trouvé
    }

    public String updateRestaurant(String id, Restaurant restaurant) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // Sécurité : On force l'ID dans l'objet pour être sûr qu'il est enregistré
        // correctement
        restaurant.setId(id);

        // .set() va écraser les anciennes données par les nouvelles
        ApiFuture<WriteResult> writeResult = db.collection("restaurants").document(id).set(restaurant);

        return writeResult.get().getUpdateTime().toString();
    }
}
