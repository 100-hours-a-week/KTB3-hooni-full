package kakaotech.community.infrastructure.token.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import kakaotech.community.global.exception.JwtAuthenticationException;
import org.springframework.stereotype.Component;

import static kakaotech.community.global.exception.JwtAuthenticationException.JwtErrorCode.TOKEN_EXPIRED;
import static kakaotech.community.global.exception.JwtAuthenticationException.JwtErrorCode.TOKEN_INVALID;
import static kakaotech.community.global.exception.JwtAuthenticationException.JwtErrorCode.TOKEN_MISSING;

@Component
public class JwtParser {
    private final io.jsonwebtoken.JwtParser parser;

    public JwtParser(JwtSecretProvider jwtSecretProvider) {
        this.parser = Jwts.parser()
                .verifyWith(jwtSecretProvider.getKey())
                .build();
    }

    public String parseId(String token) {
        return validate(token).getPayload()
                .getSubject();
    }

    public boolean isExpired(String token) {
        try {
            // JwtParser의 구현체인 DefaultJwtParser가 parsing할 때 만료된 토큰이라면 바로 예외를 던진다 함
            // 그래서 따로 Date.before(exp) 하지 않아도 됨.
            parser.parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    private Jws<Claims> validate(String token) {
        try {
            return parser.parseSignedClaims(token);
        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException(TOKEN_MISSING);
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException(TOKEN_EXPIRED);
        } catch (MalformedJwtException | SignatureException e) {
            throw new JwtAuthenticationException(TOKEN_INVALID);
        }
    }
}
