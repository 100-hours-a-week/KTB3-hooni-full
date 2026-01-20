package kakaotech.community.global.config;

import kakaotech.community.global.security.exceptionhandler.JwtAuthenticationEntryPoint;
import kakaotech.community.global.security.filter.JwtAuthenticationFilter;
import kakaotech.community.infrastructure.token.jwt.JwtAuthenticationInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAuthenticationInjector jwtAuthenticationInjector) {

        return new JwtAuthenticationFilter(
                jwtAuthenticationEntryPoint,
                jwtAuthenticationInjector
        );
    }
}
