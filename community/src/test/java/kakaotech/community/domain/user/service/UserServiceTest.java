package kakaotech.community.domain.user.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.user.LocalUserRepository;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.domain.user.dto.UserResponse;
import kakaotech.community.global.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @MockitoBean
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void clearDB() {
        ((LocalUserRepository) userRepository).clear();
    }

    @Test
    void 회원가입_성공() {
        // given
        String email = "test@example.com";
        String password = "@Aasd1234";
        String nickname = "testuser";
        MultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        UUID mockImageId = UUID.randomUUID();
        Long mockUserId = 1L;

        when(imageService.save(any(MultipartFile.class))).thenReturn(mockImageId);

        // when
        UserResponse.Join result = userService.join(email, password, nickname, image);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(mockUserId);
        assertThat(userRepository.existsByEmail(email)).isTrue();
    }

    @Test
    void 회원가입_실패_이미_존재하는_이메일() {
        // given
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        userService.join("already@email.com", "@Aasd1234", "hello", image);

        // when then
        assertThatThrownBy(() -> userService.join("already@email.com", "@ASDFasd123", "other", image))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("이미 존재하는 이메일 혹은 닉네임입니다.");
    }

    @Test
    void 회원가입_실패_이미_존재하는_닉네임() {
        // given
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        userService.join("hello@email.com", "@Aasd1234", "already", image);

        // when then
        assertThatThrownBy(() -> userService.join("other@email.com", "@ASDFasd123", "already", image))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("이미 존재하는 이메일 혹은 닉네임입니다.");
    }
}
