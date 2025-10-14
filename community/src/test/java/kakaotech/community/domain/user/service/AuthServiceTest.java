package kakaotech.community.domain.user.service;

import kakaotech.community.domain.common.image.ImageService;
import kakaotech.community.domain.user.LocalUserRepository;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.global.exception.AuthException;
import kakaotech.community.global.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthServiceTest {
    @MockitoBean
    private ImageService imageService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void clearDBAndJoin() {
        ((LocalUserRepository) userRepository).clear();

        userService.join("test@email.com",
                "@Aasd1234", "testNick",
                new MockMultipartFile(
                        "image",
                        "test.jpg",
                        "image/jpeg",
                        "test image content".getBytes()
                ));
    }

    @Test
    void 로그인_성공() {
        // when then
        assertThatNoException().isThrownBy(() -> authService.login("test@email.com", "@Aasd1234"));
    }

    @Test
    void 로그인_실패_잘못된_이메일() {
        // when then
        assertThatThrownBy(() -> authService.login("wrong@email.com", "@Aasd1234"))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("찾을 수 없는 유저입니다.");
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() {
        // when then
        assertThatThrownBy(() -> authService.login("test@email.com", "WrongPassword"))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("잘못된 이메일 혹은 비밀번호입니다.");
    }
}
