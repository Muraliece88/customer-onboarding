package com.assign.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/***
 * Security configuration class to define access rules for different endpoints.
 */

@Configuration
public class SecurityConfig {
  private final String actuatorUri =     "/actuator/**";
  private final String notificationService= "/api/v1/notifications/**";

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(autho -> autho
        .requestMatchers(notificationService).hasRole("ADMIN")
        .requestMatchers(actuatorUri).hasRole("SUPERUSER")
        .anyRequest().authenticated()
      );
    http.formLogin(Customizer.withDefaults());
    http.httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
