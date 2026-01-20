package kakaotech.community.domain.auth.service;

import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.port.TokenGenerator;
import kakaotech.community.domain.user.port.encode.Encoder;
import kakaotech.community.domain.user.service.UserService;
import kakaotech.community.global.exception.AuthException;
import kakaotech.community.global.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kakaotech.community.global.exception.code.ExceptionCode.FAILED_TO_LOGIN;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private final Encoder encoder;
    private final TokenGenerator tokenGenerator;

    public Token login(String email, String password) {
        try {
            User user = userService.findByEmail(email);
            user.validateLoginable(encoder, password);

            return tokenGenerator.login(user.getId());
        } catch (UserException e) {
            throw new AuthException(FAILED_TO_LOGIN);
        }
    }

    public Token reissue(String refreshToken) {
        return tokenGenerator.reissue(refreshToken);
    }
}
