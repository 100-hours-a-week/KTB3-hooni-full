package kakaotech.community.domain.user.port;

import kakaotech.community.domain.user.User;

public final class Token {
    private final String accessToken;

    public Token(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}
