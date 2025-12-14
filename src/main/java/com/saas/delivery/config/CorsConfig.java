package com.saas.delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Autoriser les Cookies / Authentification
        config.setAllowCredentials(true);

        // 2. Autoriser Vercel (et localhost pour tes tests)
        // Utilise setAllowedOriginPatterns au lieu de setAllowedOrigins
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:4200",
                "https://*.vercel.app" // La joker card magique
        ));

        // 3. Tout autoriser pour les Headers et Méthodes
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
