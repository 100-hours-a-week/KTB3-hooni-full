package kakaotech.community.domain.user.port;

import lombok.Getter;

@Getter
public final class Token {
    private final String accessToken;
    private final String refreshToken;

    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
