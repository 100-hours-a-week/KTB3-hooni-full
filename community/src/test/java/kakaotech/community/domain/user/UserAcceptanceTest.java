package kakaotech.community.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakaotech.community.global.common.Fixtures;
import kakaotech.community.global.exception.code.ExceptionCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_ARGUMENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    class 프로필_이미지_변경_테스트 {

    }

    @Nested
    class 비밀번호_변경_테스트 {

    }

    @Nested
    class 계정_삭제_테스트 {

    }

    @Nested
    class 나의_프로필_조회_테스트 {

    }
}
