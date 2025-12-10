package com.saas.delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. D'abord, on active CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. On désactive CSRF (obligatoire pour les POST API)
                .csrf(csrf -> csrf.disable())
                // 3. On autorise tout le monde sans login
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // AUTORISER TOUTES LES ORIGINES (Vercel, Localhost, etc.)
        configuration.setAllowedOriginPatterns(List.of("*"));

        // AUTORISER TOUTES LES MÉTHODES (IMPORTANT : OPTIONS est vital pour le Preflight)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // AUTORISER TOUS LES HEADERS (Authorization, Content-Type...)
        configuration.setAllowedHeaders(List.of("*"));

        // Autoriser les credentials (cookies, auth headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}