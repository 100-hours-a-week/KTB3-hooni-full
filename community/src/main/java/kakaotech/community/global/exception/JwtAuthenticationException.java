package kakaotech.community.global.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(JwtErrorCode jwtErrorCode) {
        super(jwtErrorCode.message);
    }

    public enum JwtErrorCode {
        TOKEN_MISSING("토큰이 누락되어있음"),
        TOKEN_EXPIRED("만료된 토큰"),
        TOKEN_INVALID("잘못된 토큰"),
        ;

        private final String message;

        JwtErrorCode(String message) {
            this.message = message;
        }
    }
}
