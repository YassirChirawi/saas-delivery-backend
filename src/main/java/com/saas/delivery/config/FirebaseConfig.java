package com.saas.delivery.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Évite de recharger si déjà lancé
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        FirebaseOptions options;
        String configJson = System.getenv("FIREBASE_CONFIG_JSON");

        // --- DEBUG LOGS (Apparaîtront dans la console Render) ---
        if (configJson != null) {
            System.out.println("✅ FOUND ENV VAR 'FIREBASE_CONFIG_JSON'. Length: " + configJson.length());
        } else {
            System.out.println("⚠️ ENV VAR 'FIREBASE_CONFIG_JSON' IS NULL. Falling back to file...");
        }
        // --------------------------------------------------------

        if (configJson != null && !configJson.isEmpty()) {
            // MODE PROD (Render)
            try (InputStream is = new ByteArrayInputStream(configJson.getBytes(StandardCharsets.UTF_8))) {
                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(is))
                        .build();
            }
        } else {
            // MODE DEV (Localhost)
            // On vérifie si le fichier existe avant de planter
            ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
            if (!resource.exists()) {
                throw new RuntimeException(
                        "❌ CRITICAL: No 'FIREBASE_CONFIG_JSON' env var AND no 'serviceAccountKey.json' file found.");
            }

            try (InputStream serviceAccount = resource.getInputStream()) {
                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
            }
        }

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public com.google.firebase.messaging.FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return com.google.firebase.messaging.FirebaseMessaging.getInstance(firebaseApp);
    }
}