package com.assign.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Security configurations
 */


@Configuration
public class SecurityConfig {

    private final String actuatorUri = "/actuator/**";
    private final String onboardingService = "/api/v1/customer-onboarding/**";
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(autho -> autho
                        .requestMatchers(onboardingService).hasRole("ADMIN")
                        .requestMatchers(actuatorUri).hasRole("SUPERUSER")
                        .anyRequest().authenticated()
                );
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}