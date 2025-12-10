package com.saas.delivery.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Toujours désactiver CSRF pour API
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Tout autoriser
                );

        return http.build();
    }

    // 🔥 LA SOLUTION NUCLÉAIRE : Ce filtre passe AVANT Spring Security
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Autoriser le frontend Vercel (et localhost pour dev)
        config.setAllowCredentials(true);
        // Utilise addAllowedOriginPattern("*") au lieu de setAllowedOrigins pour éviter les conflits
        config.addAllowedOriginPattern("*");

        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // C'est cette ligne qui change tout : Priorité MAXIMALE
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}