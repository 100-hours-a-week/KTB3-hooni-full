package kakaotech.community.domain.user.port;

public interface TokenGenerator {

    Token login(Long userId);

    Token reissue(String refreshToken);
}
