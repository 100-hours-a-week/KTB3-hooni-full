package kakaotech.community.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakaotech.community.domain.user.dto.UserRequest;
import kakaotech.community.domain.user.dto.UserResponse;
import kakaotech.community.domain.user.port.Token;
import kakaotech.community.domain.user.service.AuthService;
import kakaotech.community.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @Nested
    class LoginValidationTest {

        @Test
        void 로그인_성공() throws Exception {
            // given
            UserRequest.Login request = new UserRequest.Login(
                    "test@example.com",
                    "@Aasd1234"
            );

            when(authService.login(anyString(), anyString())).thenReturn(new Token("something"));

            // when then
            mockMvc.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "testexam.com", "t@est@exam.com", "test@examplecom"})
        void 로그인_실패_잘못된_이메일_형식(String email) throws Exception {
            // given
            UserRequest.Login request = new UserRequest.Login(
                    email,
                    "@Aasd1234"
            );

            // when then
            mockMvc.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "@ABCabcd", "ABCDabcd123", "@abcd1234", "@ABCD1234", "@ABCDEFabcdef12345678"})
        void 로그인_실패_잘못된_비밀번호_형식(String password) throws Exception {
            // given
            UserRequest.Login request = new UserRequest.Login(
                    "test@example.com",
                    password
            );

            // when then
            mockMvc.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class JoinValidationTest {

        @Test
        void 회원가입_성공() throws Exception {
            // given
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes()
            );

            when(userService.join(anyString(), anyString(), anyString(), any()))
                    .thenReturn(new UserResponse.Join(1L));

            // when then
            mockMvc.perform(multipart("/users")
                            .file(image)
                            .param("email", "test@example.com")
                            .param("password", "@Aasd1234")
                            .param("nickname", "testuser"))
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "testexam.com", "t@est@exam.com", "test@examplecom"})
        void 회원가입_실패_잘못된_이메일_형식(String email) throws Exception {
            // given
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes()
            );

            // when then
            mockMvc.perform(multipart("/users")
                            .file(image)
                            .param("email", email)
                            .param("password", "@Aasd1234")
                            .param("nickname", "testuser"))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "@ABCabcd", "ABCDabcd123", "@abcd1234", "@ABCD1234", "@ABCDEFabcdef12345678"})
        void 회원가입_실패_잘못된_비밀번호_형식(String password) throws Exception {
            // given
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes()
            );

            // when then
            mockMvc.perform(multipart("/users")
                            .file(image)
                            .param("email", "test@example.com")
                            .param("password", password)
                            .param("nickname", "testuser"))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "nick name", "nicknamenickname"})
        void nicknameWithSpace_shouldReturn400(String nickname) throws Exception {
            // given
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes()
            );

            // when then
            mockMvc.perform(multipart("/users")
                            .file(image)
                            .param("email", "test@example.com")
                            .param("password", "@Aasd1234")
                            .param("nickname", nickname))
                    .andExpect(status().isBadRequest());
        }
    }
}
