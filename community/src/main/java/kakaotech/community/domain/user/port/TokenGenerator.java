package kakaotech.community.domain.user.port;

import kakaotech.community.domain.user.User;

public interface TokenGenerator {

    // TODO. 사용자 클레임 통해 토큰 발급 필요
    Token login(Long userId);
}
