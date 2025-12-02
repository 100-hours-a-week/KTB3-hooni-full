package kakaotech.community.global.common;

import kakaotech.community.domain.image.ImageStorage;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.domain.user.port.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class Fixtures {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private ImageStorage imageStorage;

    // 사용자
    public String 토큰_발행(User user) {
        return tokenGenerator.login(user.getId()).getAccessToken();
    }

    public String 리프레시_토큰_발행(User user) {
        return tokenGenerator.login(user.getId()).getRefreshToken();
    }

    public User 사용자_생성() {
        UUID imageId = 이미지_생성();
        return userRepository.save(UserFixture.one(imageId));
    }

    // 이미지
    public UUID 이미지_생성() {
        UUID uuid = UUID.randomUUID();
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "something image".getBytes()
        );

        return imageStorage.upload(uuid, image);
    }

}
