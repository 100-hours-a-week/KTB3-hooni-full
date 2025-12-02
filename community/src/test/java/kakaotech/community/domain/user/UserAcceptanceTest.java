package kakaotech.community.domain.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.port.encode.Encoder;
import kakaotech.community.global.common.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Stream;

import static kakaotech.community.global.exception.code.ExceptionCode.DUPLICATED_EMAIL_OR_NICKNAME;
import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_ARGUMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Fixtures fixtures;

    @Autowired
    private ImageService imageService;

    @Autowired
    private Encoder encoder;

    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        user = fixtures.사용자_생성();
        accessToken = fixtures.토큰_발행(user);
    }

    @Nested
    class 회원가입_테스트 {
        private final String url = "/users";

        @Test
        void 회원가입_성공() throws Exception {
            // given
            String email = "test@email.com";
            String password = "@ASDasd123";
            String nickname = "testNick";
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "profile.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "profile Image".getBytes()
            );

            // when then
            mockMvc.perform(
                            multipart(url)
                                    .file(image)
                                    .param("email", email)
                                    .param("password", password)
                                    .param("nickname", nickname)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andReturn();

        }

        @ParameterizedTest
        @MethodSource("invalidInput")
        void 회원가입_실패_잘못된_입력_형태_이미지는_정상(String email, String password, String nickname) throws Exception {
            // given
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "profile.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "profile Image".getBytes()
            );

            // when then
            mockMvc.perform(
                            multipart(url)
                                    .file(image)
                                    .param("email", email)
                                    .param("password", password)
                                    .param("nickname", nickname)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        static Stream<Arguments> invalidInput() {
            return Stream.of(
                    Arguments.arguments("email.com", "@ASDasd123", "testNick"),
                    Arguments.arguments("email@email.com", "asd123456", "testNick"),
                    Arguments.arguments("email@email.com", "@ASas12", "testNick"),
                    Arguments.arguments("email@email.com", "@ASDasd123", "testNicknick"),
                    Arguments.arguments("email@email.com", "@ASDasd123", "test Nick")
            );
        }

        @Test
        void 회원가입_실패_이미지_없음() throws Exception {
            // given
            String email = "test@email.com";
            String password = "@ASDasd123";
            String nickname = "testNick";

            // when then
            mockMvc.perform(
                            multipart(url)
                                    .param("email", email)
                                    .param("password", password)
                                    .param("nickname", nickname)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @Test
        void 회원가입_실패_빈_이미지() throws Exception {
            // given
            String email = "test@email.com";
            String password = "@ASDasd123";
            String nickname = "testNick";
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "empty.png",
                    MediaType.IMAGE_PNG_VALUE,
                    new byte[0]
            );

            // when then
            mockMvc.perform(
                            multipart(url)
                                    .file(file)
                                    .param("email", email)
                                    .param("password", password)
                                    .param("nickname", nickname)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @ParameterizedTest
        @MethodSource("alreadyExists")
        void 회원가입_실패_이미_존재하는_이메일_혹은_닉네임(String email, String nickname) throws Exception {
            String password = "@ASDasd123";
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "profile.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "profile Image".getBytes()
            );

            // when then
            mockMvc.perform(
                            multipart(url)
                                    .file(image)
                                    .param("email", email)
                                    .param("password", password)
                                    .param("nickname", nickname)
                    )
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value(DUPLICATED_EMAIL_OR_NICKNAME.getMessage()));
        }

        static Stream<Arguments> alreadyExists() {
            return Stream.of(
                    Arguments.arguments("email1@email.com", "validNick"),
                    Arguments.arguments("valid@email.com", "test1")
            );
        }
    }

    @Nested
    class 프로필_변경_테스트 {
        private final String url = "/users/me";

        @Test
        void 둘다_변경_성공() throws Exception {
            // given
            String newNickname = "newNick";
            byte[] image = "my new Image".getBytes();
            MockMultipartFile newImage = new MockMultipartFile(
                    "image",
                    "newImage.png",
                    MediaType.IMAGE_PNG_VALUE,
                    image
            );

            // when
            MvcResult result = mockMvc.perform(
                            multipart(HttpMethod.PATCH, url)
                                    .file(newImage)
                                    .param("nickname", newNickname)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            UUID imageId = UUID.fromString(root.get("profileImage").asText());
            byte[] imageBytes = imageService.load(imageId).resource().getContentAsByteArray();

            assertAll(
                    () -> assertThat(imageBytes).isEqualTo(image),
                    () -> assertThat(root.get("nickname").asText()).isEqualTo(newNickname)
            );
        }

        @Test
        void 이미지만_변경_성공() throws Exception {
            // given
            String newNickname = "newNick";

            // when
            MvcResult result = mockMvc.perform(
                            multipart(HttpMethod.PATCH, url)
                                    .param("nickname", newNickname)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            assertThat(root.get("nickname").asText()).isEqualTo(newNickname);
        }

        @Test
        void 닉네임만_변경_성공() throws Exception {
            // given
            byte[] image = "my new Image".getBytes();
            MockMultipartFile newImage = new MockMultipartFile(
                    "image",
                    "newImage.png",
                    MediaType.IMAGE_PNG_VALUE,
                    image
            );

            // when
            MvcResult result = mockMvc.perform(
                            multipart(HttpMethod.PATCH, url)
                                    .file(newImage)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            UUID imageId = UUID.fromString(root.get("profileImage").asText());
            byte[] imageBytes = imageService.load(imageId).resource().getContentAsByteArray();

            assertThat(imageBytes).isEqualTo(image);
        }

        @ParameterizedTest
        @ValueSource(strings = {"new nick", "newNickname"})
        void 변경_실패_닉네임_형식_틀림(String newNickname) throws Exception {
            // when
            mockMvc.perform(
                            multipart(HttpMethod.PATCH, url)
                                    .param("nickname", newNickname)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));

        }
    }

    @Nested
    class 비밀번호_변경_테스트 {
        private final String url = "/users/me/change-password";

        @Test
        void 변경_성공() throws Exception {
            // given
            String newPassword = "q1w2e3r4!@QW";
            UserRequest.ChangePw request = new UserRequest.ChangePw(newPassword);

            // when
            mockMvc.perform(
                            patch(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNoContent());

            // then
            assertThat(encoder.matches(newPassword, user.getPassword())).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"@As12", "abcdef12345", "", " "})
        void 변경_실패_잘못된_형식(String wrongPw) throws Exception {
            // given
            UserRequest.ChangePw request = new UserRequest.ChangePw(wrongPw);

            // when then
            mockMvc.perform(
                    patch(url)
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage())
            );
        }
    }

    @Nested
    class 계정_삭제_테스트 {

    }

    @Nested
    class 나의_프로필_조회_테스트 {

    }
}
