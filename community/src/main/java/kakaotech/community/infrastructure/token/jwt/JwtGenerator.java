package kakaotech.community.infrastructure.token.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.port.TokenGenerator;
import kakaotech.community.infrastructure.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Primary // FIXME. 프로필로 분리하여 설정되도록 수정 필요
@Component
@RequiredArgsConstructor
public class JwtGenerator implements TokenGenerator {
    private final JwtSecretProvider jwtSecretProvider;

    private final RefreshTokenService refreshTokenService;

    @Override
    public Token login(Long userId) {
        return new Token(generateAccessToken(userId), refreshTokenService.issue(userId));
    }

    private String generateAccessToken(Long userId) {
        Claims claims = Jwts.claims()
                .subject(String.valueOf(userId))
                .add(Map.of(
                        "tokenType", "ACCESS",
                        Claims.EXPIRATION, jwtSecretProvider.getAccessExpiration()
                ))
                .build();

        return Jwts.builder()
                .header() // header
                    .type("JWT")
                    .and()

                .claims(claims) // payload

                .signWith(jwtSecretProvider.getKey()) // sign
                .compact();
    }
}
