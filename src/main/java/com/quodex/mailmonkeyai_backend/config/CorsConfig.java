package com.quodex.mailmonkeyai_backend.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

  @Value("${frontend.url}")
  private String frontendUrls; // comma-separated URLs (e.g. https://dine-board.vercel.app,https://localhost:5173)

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // ✅ Split by comma and trim spaces
    List<String> allowedOrigins = Arrays.stream(frontendUrls.split(","))
      .map(String::trim)
      .toList();

    // ✅ Set origins directly (not nested)
    config.setAllowedOrigins(allowedOrigins);

    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}
