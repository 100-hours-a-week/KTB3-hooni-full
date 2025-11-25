package kakaotech.community.infrastructure.token;

import io.jsonwebtoken.Jwts;
import kakaotech.community.domain.auth.RefreshToken;
import kakaotech.community.domain.auth.RefreshTokenRepository;
import kakaotech.community.infrastructure.token.jwt.JwtParser;
import kakaotech.community.infrastructure.token.jwt.JwtSecretProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtSecretProvider jwtSecretProvider;
    private final JwtParser jwtParser;

    public String issue(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .map(this::getPayload)
                .orElseGet(() -> createRefreshToken(userId).getPayload());
    }

    private String getPayload(RefreshToken refreshToken) {
        return jwtParser.isExpired(refreshToken.getPayload()) ? refreshToken.reissue(generateRefreshToken()) : refreshToken.getPayload();
    }

    private RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken(userId, generateRefreshToken());
        return refreshTokenRepository.save(refreshToken);
    }

    private String generateRefreshToken() {
        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()

                .expiration(jwtSecretProvider.getRefreshExpiration())

                .signWith(jwtSecretProvider.getKey())
                .compact();
    }
}
