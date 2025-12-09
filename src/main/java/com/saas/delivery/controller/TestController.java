package com.saas.delivery.controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test-firestore")
    public String testFirestore() {
        try {
            Firestore db = FirestoreClient.getFirestore();

            // On crée une donnée bidon
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Hello Firebase from Spring Boot!");
            data.put("timestamp", System.currentTimeMillis());

            // On l'écrit dans une collection "tests"
            db.collection("tests").add(data);

            return "Succès ! Vérifie ta console Firebase, collection 'tests'.";
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}