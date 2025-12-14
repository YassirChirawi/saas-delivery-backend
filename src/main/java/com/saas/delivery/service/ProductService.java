package com.saas.delivery.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.saas.delivery.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProductService {

    private static final String COLLECTION_NAME = "products";

    // Méthode pour créer un produit
    public String createProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // On crée une référence pour générer un ID automatique
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();

        // On assigne cet ID à notre objet Java
        product.setId(docRef.getId());

        // On sauvegarde dans Firebase (.set est asynchrone, .get() attend la fin)
        ApiFuture<WriteResult> result = docRef.set(product);

        return result.get().getUpdateTime().toString();
    }

    // Méthode pour récupérer tous les produits
    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // On demande tous les documents de la collection
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Product> productList = new ArrayList<>();

        for (DocumentSnapshot document : documents) {
            // Firebase convertit automatiquement le JSON en objet Java !
            Product product = document.toObject(Product.class);
            productList.add(product);
        }

        return productList;
    }
    // Importe bien les classes Query et CollectionReference
    public List<Product> getProductsByRestaurant(String restaurantId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // Requête : Select * from products WHERE restaurantId = X
        CollectionReference products = db.collection("products");
        Query query = products.whereEqualTo("restaurantId", restaurantId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Product> productList = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
            Product product = doc.toObject(Product.class);
            product.setId(doc.getId());
            productList.add(product);
        }
        return productList;
    }

    public String updateProduct(String id, Product product) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("products").document(id).set(product);
        return writeResult.get().getUpdateTime().toString();
    }

    public String deleteProduct(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("products").document(id).delete();
        return "Product deleted " + id;
    }
}