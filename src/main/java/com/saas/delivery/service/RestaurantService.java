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

    public String createRestaurant(Restaurant restaurant) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        // On force l'ID pour qu'il soit joli dans l'URL (slug)
        // ex: "Chez Mario" -> "chez-mario" (A faire côté front ou ici)
        DocumentReference docRef = db.collection(COLLECTION).document(restaurant.getId());
        docRef.set(restaurant);
        return restaurant.getId();
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
}