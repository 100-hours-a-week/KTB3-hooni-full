package kakaotech.community.domain.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakaotech.community.domain.image.dto.ImageMeta;
import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.user.User;
import kakaotech.community.global.common.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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

import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_ARGUMENT;
import static kakaotech.community.global.exception.code.ExceptionCode.POST_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.POST_WRITER_MISMATCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Fixtures fixtures;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        user = fixtures.사용자_생성();
        accessToken = fixtures.토큰_발행(user);
    }

    @Nested
    class 게시글_생성_테스트 {
        private final String url = "/posts";

        @Test
        void 생성_성공_이미지_포함() throws Exception {
            // given
            String title = "test title";
            String content = "test content";
            byte[] imageBytes = "My Post Image".getBytes();
            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "image.png",
                    MediaType.IMAGE_PNG_VALUE,
                    imageBytes
            );

            // when
            MvcResult result = mockMvc.perform(
                            multipart(url)
                                    .file(image)
                                    .param("title", title)
                                    .param("content", content)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isCreated())
                    .andReturn();

            // then
            String resultContent = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(resultContent);

            ImageMeta meta = imageService.load(UUID.fromString(root.get("image").asText()));

            assertAll(
                    () -> assertThat(root.get("writerId").asLong()).isEqualTo(user.getId()),
                    () -> assertThat(root.get("postId")).isNotNull(),
                    () -> assertThat(root.get("postId").isIntegralNumber()).isTrue(),
                    () -> assertThat(root.get("title").asText()).isEqualTo(title),
                    () -> assertThat(root.get("content").asText()).isEqualTo(content),
                    () -> assertThat(meta.resource().getContentAsByteArray()).isEqualTo(imageBytes)
            );
        }

        @Test
        void 생성_성공_이미지_미포함() throws Exception {
            {
                // given
                String title = "test title";
                String content = "test content";

                // when
                MvcResult result = mockMvc.perform(
                                multipart(url)
                                        .param("title", title)
                                        .param("content", content)
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        )
                        .andExpect(status().isCreated())
                        .andReturn();

                // then
                String resultContent = result.getResponse().getContentAsString();
                JsonNode root = objectMapper.readTree(resultContent);

                assertAll(
                        () -> assertThat(root.get("writerId").asLong()).isEqualTo(user.getId()),
                        () -> assertThat(root.get("postId")).isNotNull(),
                        () -> assertThat(root.get("postId").isIntegralNumber()).isTrue(),
                        () -> assertThat(root.get("title").asText()).isEqualTo(title),
                        () -> assertThat(root.get("content").asText()).isEqualTo(content),
                        () -> assertThat(root.get("image").isNull()).isTrue()
                );
            }
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 생성_실패_제목_Null_Or_Empty(String title) throws Exception {
            // given
            String content = "test content";

            // when
            mockMvc.perform(
                            multipart(url)
                                    .param("title", title)
                                    .param("content", content)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 생성_실패_내용_Null_Or_Empty(String content) throws Exception {
            // given
            String title = "test title";

            // when
            mockMvc.perform(
                            multipart(url)
                                    .param("title", title)
                                    .param("content", content)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @Test
        void 생성_실패_제목_글자_26자_초과() throws Exception {
            // given
            String wrongTitle = "title over 27 will throw ex";
            String content = "test content";

            // when
            mockMvc.perform(
                            multipart(url)
                                    .param("title", wrongTitle)
                                    .param("content", content)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }
    }

    @Nested
    class 게시글_수정_테스트 {
        private final String url = "/posts/{postId}";

        @Test
        void 수정_성공_이미지_없는_글에다가_이미지_추가() throws Exception {
            // given
            Post post = fixtures.게시글_생성_이미지_없는(user);

            String newContent = "updated content";
            byte[] newImageBytes = "newImage in my Post".getBytes();
            MockMultipartFile newImage = new MockMultipartFile(
                    "image",
                    "newImage.png",
                    MediaType.IMAGE_PNG_VALUE,
                    newImageBytes
            );

            // when
            MvcResult result = mockMvc.perform(
                            multipart(HttpMethod.PUT, url, post.getId())
                                    .file(newImage)
                                    .param("title", post.getTitle())
                                    .param("content", newContent)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String resultContent = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(resultContent);

            ImageMeta meta = imageService.load(UUID.fromString(root.get("image").asText()));

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("content").asText()).isEqualTo(newContent),
                    () -> assertThat(meta.resource().getContentAsByteArray()).isEqualTo(newImageBytes)
            );
        }

        @Test
        void 수정_성공_이미지_없는_글_이미지_없이_수정() throws Exception {
            // given
            Post post = fixtures.게시글_생성_이미지_없는(user);
            String newTitle = "updated title";

            // when
            MvcResult result = mockMvc.perform(
                            multipart(HttpMethod.PUT, url, post.getId())
                                    .param("title", newTitle)
                                    .param("content", post.getContent())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String resultContent = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(resultContent);

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("title").asText()).isEqualTo(newTitle),
                    () -> assertThat(root.get("image").isNull()).isTrue()
            );
        }

        @Test
        void 수정_성공_이미지_있는_글에_이미지_값_덮어쓰기() throws Exception {
            // given
            Post post = fixtures.게시글_생성(user);
            byte[] newImageBytes = "new Image png".getBytes();
            MockMultipartFile newImage = new MockMultipartFile(
                    "image",
                    "newImage.png",
                    MediaType.IMAGE_PNG_VALUE,
                    newImageBytes
            );

            // when
            MvcResult result = mockMvc.perform(
                            multipart(HttpMethod.PUT, url, post.getId())
                                    .file(newImage)
                                    .param("title", post.getTitle())
                                    .param("content", post.getContent())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            String resultContent = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(resultContent);

            ImageMeta meta = imageService.load(UUID.fromString(root.get("image").asText()));

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(meta.resource().getContentAsByteArray()).isEqualTo(newImageBytes)
            );
        }

        @Test
        void 수정_성공_이미지_있는_글에_이미지_미포함_수정요청_이미지_없어짐() throws Exception {
            // given
            Post post = fixtures.게시글_생성(user);

            // when
            MvcResult result = mockMvc.perform(
                            multipart(HttpMethod.PUT, url, post.getId())
                                    .param("title", post.getTitle())
                                    .param("content", post.getContent())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            String resultContent = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(resultContent);

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("image").isNull()).isTrue()
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 수정_실패_제목_Null_Or_Empty(String title) throws Exception {
            // given
            Post post = fixtures.게시글_생성(user);

            // when
            mockMvc.perform(
                            multipart(HttpMethod.PUT, url, post.getId())
                                    .param("title", title)
                                    .param("content", post.getContent())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @Test
        void 수정_실패_제목_글자_26자_초과() throws Exception {
            // given
            Post post = fixtures.게시글_생성(user);

            String wrongTitle = "title over 27 will throw ex";

            // when
            mockMvc.perform(
                            multipart(HttpMethod.PUT, url, post.getId())
                                    .param("title", wrongTitle)
                                    .param("content", post.getContent())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 수정_실패_내용_Null_Or_Empty(String content) throws Exception {
            // given
            Post post = fixtures.게시글_생성(user);

            // when
            mockMvc.perform(
                            multipart(HttpMethod.PUT, url, post.getId())
                                    .param("title", post.getTitle())
                                    .param("content", content)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));

        }

        @Test
        void 수정_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongId = 9999L;
            assertThat(postRepository.existsById(wrongId)).isFalse();

            // when
            mockMvc.perform(
                            multipart(HttpMethod.PUT, url, wrongId)
                                    .param("title", "title")
                                    .param("content", "content")
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
        }

        @Test
        void 수정_실패_수정_권한_없는_타인의_게시글() throws Exception {
            // given
            User notMe = fixtures.다른_사용자_생성();
            Post notMyPost = fixtures.게시글_생성(notMe);

            // when
            mockMvc.perform(
                            multipart(HttpMethod.PUT, url, notMyPost.getId())
                                    .param("title", "title")
                                    .param("content", "content")
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(POST_WRITER_MISMATCH.getMessage()));
        }
    }

    @Nested
    class 게시글_삭제_테스트 {
        private final String url = "/posts/{postId}";

        @Test
        void 삭제_성공() throws Exception {
            // given
            Post post = fixtures.게시글_생성(user);

            // when
            mockMvc.perform(
                            delete(url, post.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNoContent());

            assertThat(postRepository.existsById(post.getId())).isFalse();
        }

        @Test
        void 삭제_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongPostId = 9999L;
            assertThat(postRepository.existsById(wrongPostId)).isFalse();

            // when
            mockMvc.perform(
                            delete(url, wrongPostId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
        }

        @Test
        void 삭제_실패_삭제_권한_없는_타인의_게시글() throws Exception {
            // given
            User notMe = fixtures.다른_사용자_생성();
            Post notMyPost = fixtures.게시글_생성(notMe);

            // when
            mockMvc.perform(
                            delete(url, notMyPost.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(POST_WRITER_MISMATCH.getMessage()));

            // then
            assertThat(postRepository.existsById(notMyPost.getId())).isTrue();
        }
    }

    @Nested
    class 게시글_단건_조회_테스트 {

    }

    @Nested
    class 게시글_페이징_조회_테스트 {

    }


}
