package kakaotech.community.domain.auth.controller;

import jakarta.validation.Valid;
import kakaotech.community.domain.auth.service.AuthService;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.global.apidoc.AuthApiDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApiDocs {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Token> login(@Valid @RequestBody UserRequest.Login request) {
        return ResponseEntity.ok(authService.login(request.email(), request.password()));
    }
}
