package com.saas.delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactiver la protection CSRF (inutile pour les API REST utilisées par Angular/Mobile)
                .csrf(csrf -> csrf.disable())

                // 2. Activer le CORS (pour que ton fichier CorsConfig soit pris en compte)
                .cors(Customizer.withDefaults())

                // 3. Autoriser l'accès
                .authorizeHttpRequests(auth -> auth
                        // Autorise l'accès public à toutes les URL (pour tester)
                        .requestMatchers("/**").permitAll()
                );

        return http.build();
    }
}