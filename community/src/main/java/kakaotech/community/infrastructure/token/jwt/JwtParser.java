package kakaotech.community.infrastructure.token.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtParser {
    private final io.jsonwebtoken.JwtParser parser;

    public JwtParser(JwtSecretProvider jwtSecretProvider) {
        this.parser = Jwts.parser()
                .verifyWith(jwtSecretProvider.getKey())
                .build();
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
}
