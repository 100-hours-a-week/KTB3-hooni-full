package kakaotech.community.domain.auth.service;

import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.port.TokenGenerator;
import kakaotech.community.domain.user.port.encode.Encoder;
import kakaotech.community.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private final Encoder encoder;
    private final TokenGenerator tokenGenerator;

    // FIXME. 이메일 틀린 시 UserException 발생 -> AuthException으로 변경 필요
    public Token login(String email, String password) {
        User user = userService.findByEmail(email);
        user.validateLoginable(encoder, password);

        return tokenGenerator.login(user.getId());
    }
}
