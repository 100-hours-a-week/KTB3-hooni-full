package kakaotech.community.infrastructure.jwt;

import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.port.TokenGenerator;
import org.springframework.stereotype.Component;

@Component
public class JwtMockGenerator implements TokenGenerator {

    @Override
    public Token login(Long userId) {
        return new Token(String.valueOf(userId), String.valueOf(userId));
    }
}
