package kakaotech.community.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    private static final List<String> ALLOWED_ORIGINS = List.of("http://127.0.0.1:5500", "http://localhost:5500");
    private static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    private static final List<String> ALLOWED_HEADERS = List.of("Authorization", "Content-Type", "Accept");
    private static final String allowedPattern = "/**";

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
         CorsConfiguration config = new CorsConfiguration();
         config.setAllowedOrigins(ALLOWED_ORIGINS);
         config.setAllowedMethods(ALLOWED_METHODS);
         config.setAllowedHeaders(ALLOWED_HEADERS);
         config.setAllowCredentials(true);

         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration(allowedPattern, config);
         return source;
    }
}
