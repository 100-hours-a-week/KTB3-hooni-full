package kakaotech.community.domain.user.controller;

import jakarta.validation.Valid;
import kakaotech.community.domain.common.validator.Password;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.dto.UserResponse;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.service.AuthService;
import kakaotech.community.domain.user.service.UserService;
import kakaotech.community.global.auth.annotation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/me")
    public ResponseEntity<UserResponse.Update> changeProfile(@Authenticated Long userId,
                                             @Valid @ModelAttribute UserRequest.Update request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request.nickname(), request.image()));
    }

    @PatchMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@Authenticated Long userId, @Valid @RequestBody UserRequest.ChangePw request) {
        userService.changePassword(userId, request.password());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> delete(@Authenticated Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
