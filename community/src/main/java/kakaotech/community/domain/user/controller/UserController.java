package kakaotech.community.domain.user.controller;

import jakarta.validation.Valid;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.dto.UserResponse;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.service.AuthService;
import kakaotech.community.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Token> login(@Valid @RequestBody UserRequest.Login request) {
        return ResponseEntity.ok(authService.login(request.email(), request.password()));
    }

    @PostMapping
    public ResponseEntity<UserResponse.Join> join(@Valid @ModelAttribute UserRequest.Join request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.join(request.email(), request.password(), request.nickname(), request.image()));
    }
}
