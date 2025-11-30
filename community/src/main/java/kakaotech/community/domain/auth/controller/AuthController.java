package kakaotech.community.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kakaotech.community.domain.auth.service.AuthService;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.global.apidoc.AuthApiDocs;
import kakaotech.community.global.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static kakaotech.community.global.exception.code.ExceptionCode.AUTH_TOKEN_NOT_FOUND;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.Login request, HttpServletResponse response) {
        Token token = authService.login(request.email(), request.password());

        setCookie(response, token.getRefreshToken());

        return ResponseEntity.ok(Map.of("accessToken", token.getAccessToken()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AUTH_TOKEN_NOT_FOUND);
        }

        Token token = authService.reissue(refreshToken);

        setCookie(response, token.getRefreshToken());

        return ResponseEntity.ok(Map.of("accessToken", token.getAccessToken()));
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/reissue");
        response.addCookie(cookie);
    }
}
