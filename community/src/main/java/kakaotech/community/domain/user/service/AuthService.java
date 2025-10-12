package kakaotech.community.domain.user.service;

import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.port.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private final TokenGenerator tokenGenerator;

    public Token login(String email, String password) {
        User user = userService.findByEmail(email);
        user.validateLoginable(password);

        return tokenGenerator.login(user.getId());
    }
}
