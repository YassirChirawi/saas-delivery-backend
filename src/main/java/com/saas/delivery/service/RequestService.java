package com.saas.delivery.service;

// 1. Imports Firebase / Firestore (Google Cloud)
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

// 2. Import de ton Modèle
import com.saas.delivery.model.PartnerRequest;

// 3. Imports Spring Boot
import org.springframework.stereotype.Service;

// 4. Imports Java Standard (Listes et Exceptions)
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RequestService {

    public String createRequest(PartnerRequest request) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // On crée un nouveau document dans la collection "requests"
        // .document() sans argument génère un ID automatique
        DocumentReference docRef = db.collection("requests").document();

        // On sauvegarde l'ID généré dans l'objet pour référence future
        request.setId(docRef.getId());

        ApiFuture<WriteResult> result = docRef.set(request);

        return result.get().getUpdateTime().toString();
    }

    public List<PartnerRequest> getAllRequests() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        // On récupère tout (tu pourras ajouter .whereEqualTo("status", "PENDING") plus tard)
        ApiFuture<QuerySnapshot> future = db.collection("requests").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<PartnerRequest> requests = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            PartnerRequest req = document.toObject(PartnerRequest.class);
            req.setId(document.getId());
            requests.add(req);
        }
        return requests;
    }

    // Mettre à jour (pour changer le statut ou modifier les infos)
    // Utile aussi pour supprimer la demande après validation
    public String deleteRequest(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("requests").document(id).delete();
        return "Deleted";
    }
}