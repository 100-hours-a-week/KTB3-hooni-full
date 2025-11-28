package kakaotech.community.global.config;

import kakaotech.community.global.security.exceptionhandler.JwtAuthenticationEntryPoint;
import kakaotech.community.global.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] ALLOWED_URL_LIST = new String[]{"/swagger**", "/auth/login", "/users"};

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(ALLOWED_URL_LIST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts", "/posts/**").permitAll()
                        .anyRequest().authenticated())

                .cors(withDefaults()) // CorsConfig의 설정을 따름
                .csrf(AbstractHttpConfigurer::disable)// 헤더 Authorization에 JWT 토큰 방식이므로 csrf 토큰 불필요
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .build();
    }
}
