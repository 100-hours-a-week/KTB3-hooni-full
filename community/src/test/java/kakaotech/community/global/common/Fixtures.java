package kakaotech.community.global.common;

import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.domain.user.port.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class Fixtures {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    public String 토큰_발행(User user) {
        return tokenGenerator.login(user.getId()).getAccessToken();
    }

    public String 리프레시_토큰_발행(User user) {
        return tokenGenerator.login(user.getId()).getRefreshToken();
    }
}
