package kakaotech.community.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakaotech.community.global.exception.JwtAuthenticationException;
import kakaotech.community.infrastructure.token.jwt.JwtAuthenticationInjector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static kakaotech.community.global.exception.JwtAuthenticationException.JwtErrorCode.TOKEN_INVALID;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationInjector jwtAuthenticationInjector;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!token.startsWith(BEARER_PREFIX)) {
            handleAuthenticationException(request, response,
                    new JwtAuthenticationException(TOKEN_INVALID));
            return;
        }

        String subToken = token.substring(BEARER_PREFIX.length());

        try {
            jwtAuthenticationInjector.setAuthentication(subToken);
        } catch (AuthenticationException ex) {
            handleAuthenticationException(request, response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws ServletException, IOException{
        authenticationEntryPoint.commence(request, response, ex);
    }
}
