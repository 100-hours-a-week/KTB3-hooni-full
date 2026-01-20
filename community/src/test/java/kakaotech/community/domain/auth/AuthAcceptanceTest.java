package kakaotech.community.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.service.UserService;
import kakaotech.community.global.common.Fixtures;
import kakaotech.community.infrastructure.token.jwt.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static kakaotech.community.global.exception.code.ExceptionCode.AUTH_TOKEN_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.FAILED_TO_LOGIN;
import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_ARGUMENT;
import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_AUTH_TOKEN;
import static kakaotech.community.global.exception.code.ExceptionCode.RE_LOGIN_REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthAcceptanceTest {
    private static final String email = "test1@email.com";
    private static final String password = "@ASDasd123";
    private static final String nickname = "test1";
    private static final MultipartFile image = new MockMultipartFile(
            "image",
            "profile.png",
            MediaType.IMAGE_PNG_VALUE,
            "dummy image".getBytes()
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Fixtures fixtures;

    @Autowired
    private UserService userService;

    @MockitoSpyBean
    JwtParser jwtParser;

    private User user;

    @BeforeEach
    void setUp() {
        var userDto = userService.join(email, password, nickname, image);
        user = userService.findById(userDto.id());
    }

    @Nested
    class 로그인_테스트 {
        private final String url = "/auth/login";

        @Test
        void 로그인_성공() throws Exception {
            // given
            UserRequest.Login request = new UserRequest.Login(email, password);

            // when
            MvcResult result = mockMvc.perform(
                            post(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.accessToken").isNotEmpty())
                    .andReturn();

            // then
            Cookie refreshToken = result.getResponse().getCookie("refreshToken");

            assertAll(
                    () -> assertThat(refreshToken).isNotNull(),
                    () -> assertThat(refreshToken.isHttpOnly()).isTrue()
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 로그인_실패_틀린_형식의_이메일(String invalidEmail) throws Exception {
            // given
            UserRequest.Login request = new UserRequest.Login(invalidEmail, password);

            // when then
            mockMvc.perform(
                            post(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @Test
        void 로그인_실패_존재하지않는_아이디() throws Exception {
            // given
            String wrongEmail = "wrong@email.com";
            UserRequest.Login request = new UserRequest.Login(wrongEmail, password);

            // when then
            mockMvc.perform(
                            post(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(FAILED_TO_LOGIN.getMessage()));
        }

        @Test
        void 로그인_실패_잘못된_비밀번호() throws Exception {
            // given
            String wrongPassword = "wrongPassword";
            UserRequest.Login request = new UserRequest.Login(email, wrongPassword);

            // when then
            mockMvc.perform(
                            post(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(FAILED_TO_LOGIN.getMessage()));
        }
    }

    @Nested
    class 토큰_재발행_테스트 {
        private final String url = "/auth/reissue";

        @Test
        void 재발행_성공() throws Exception {
            // given
            String refreshToken = fixtures.리프레시_토큰_발행(user);
            when(jwtParser.isExpired(refreshToken)).thenReturn(false);

            // when
            MvcResult result = mockMvc.perform(
                            post(url)
                                    .cookie(new Cookie("refreshToken", refreshToken))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.accessToken").isNotEmpty())
                    .andReturn();

            // then
            Cookie newRefreshToken = result.getResponse().getCookie("refreshToken");
            assertAll(
                    () -> assertThat(newRefreshToken).isNotNull(),
                    () -> assertThat(newRefreshToken.isHttpOnly()).isTrue()
            );
        }

        @Test
        void 재발급_실패_유효하지_않은_RT() throws Exception {
            // given
            String invalidRefreshToken = "a1b2c3d4e5f6";

            // when then
            mockMvc.perform(
                            post(url)
                                    .cookie(new Cookie("refreshToken", invalidRefreshToken))
                    )
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(INVALID_AUTH_TOKEN.getMessage()));
        }

        @ParameterizedTest
        @EmptySource
        void 재발급_실패_RT_빈문자열(String refreshToken) throws Exception {
            // when then
            mockMvc.perform(
                    post(url)
                            .cookie(new Cookie("refreshToken", refreshToken))
            )
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(AUTH_TOKEN_NOT_FOUND.getMessage()));
        }

        @Test
        void 재발급_실패_쿠키_없음() throws Exception {
            // when then
            mockMvc.perform(
                    post(url)
            )
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(AUTH_TOKEN_NOT_FOUND.getMessage()));
        }

        @Test
        void 재발급_실패_만료된_RT() throws Exception {
            // given
            String refreshToken = fixtures.리프레시_토큰_발행(user);
            when(jwtParser.isExpired(refreshToken)).thenReturn(true);

            // when then
            mockMvc.perform(
                    post(url)
                            .cookie(new Cookie("refreshToken", refreshToken))
            )
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(RE_LOGIN_REQUIRED.getMessage()));
        }
    }
}
