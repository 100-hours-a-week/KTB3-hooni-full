package kakaotech.community.domain.comment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakaotech.community.domain.comment.dto.CommentRequest;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static kakaotech.community.global.exception.code.ExceptionCode.COMMENT_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.COMMENT_WRITER_MISMATCH;
import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_ARGUMENT;
import static kakaotech.community.global.exception.code.ExceptionCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CommentAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Fixtures fixtures;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private String accessToken;

    private User another;
    private Post post;

    @BeforeEach
    void setUp() {
        user = fixtures.사용자_생성();
        accessToken = fixtures.토큰_발행(user);

        another = fixtures.다른_사용자_생성();
        post = fixtures.게시글_생성(another);
    }

    @Nested
    class 댓글_생성_테스트 {
        private final String url = "/posts/{postId}/comments";

        @Test
        void 생성_성공() throws Exception {
            // given
            String content = "comment content";
            CommentRequest.Write request = new CommentRequest.Write(content);

            // when
            MvcResult result = mockMvc.perform(
                            post(url, post.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isCreated())
                    .andReturn();

            // then
            String resultContent = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(resultContent);
            long commentId = root.get("commentId").asLong();

            assertAll(
                    () -> assertThat(commentRepository.findById(commentId).isPresent()).isTrue(),
                    () -> {
                        Comment comment = commentRepository.findById(commentId).get();
                        assertThat(comment.getContent()).isEqualTo(content);
                    }
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 생성_실패_댓글_형식_잘못된(String content) throws Exception {
            // given
            CommentRequest.Write request = new CommentRequest.Write(content);

            // when then
            mockMvc.perform(
                            post(url, post.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @Test
        void 생성_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongPostId = 9999L;
            assertThat(postRepository.findById(wrongPostId).isPresent()).isFalse();

            String content = "comment content";
            CommentRequest.Write request = new CommentRequest.Write(content);

            // when
            mockMvc.perform(
                            post(url, wrongPostId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
        }
    }

    @Nested
    class 댓글_수정_테스트 {
        private final String url = "/posts/{postId}/comments/{commentId}";

        @Test
        void 수정_성공() throws Exception {
            // given
            Comment comment = fixtures.댓글_생성(post, user);

            String content = "updated content";
            CommentRequest.Write request = new CommentRequest.Write(content);

            // when
            MvcResult result = mockMvc.perform(
                            patch(url, post.getId(), comment.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String resultContent = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(resultContent);

            assertAll(
                    () -> assertThat(root.get("commentId").asLong()).isEqualTo(comment.getId()),
                    () -> assertThat(comment.getContent()).isEqualTo(content)
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 수정_실패_댓글_형식_잘못됨(String content) throws Exception {
            // given
            Comment comment = fixtures.댓글_생성(post, user);

            CommentRequest.Write request = new CommentRequest.Write(content);

            // when then
            mockMvc.perform(
                            patch(url, post.getId(), comment.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(INVALID_ARGUMENT.getMessage()));
        }

        @Test
        void 수정_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongPostId = 9999L;
            assertThat(postRepository.findById(wrongPostId).isPresent()).isFalse();

            Comment comment = fixtures.댓글_생성(post, user);

            String content = "updated content";
            CommentRequest.Write request = new CommentRequest.Write(content);

            // when
            mockMvc.perform(
                            patch(url, wrongPostId, comment.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
        }

        @Test
        void 수정_실패_잘못된_댓글_ID() throws Exception {
            // given
            Long wrongCommentId = 9999L;
            assertThat(commentRepository.findById(wrongCommentId).isPresent()).isFalse();

            Comment comment = fixtures.댓글_생성(post, user);

            String content = "updated content";
            CommentRequest.Write request = new CommentRequest.Write(content);

            // when
            mockMvc.perform(
                            patch(url, post.getId(), wrongCommentId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(COMMENT_NOT_FOUND.getMessage()));
        }

        @Test
        void 수정_실패_내가_작성한_댓글이_아님() throws Exception {
            // given
            Comment comment = fixtures.댓글_생성(post, another);

            String content = "updated content";
            CommentRequest.Write request = new CommentRequest.Write(content);

            // when
            mockMvc.perform(
                            patch(url, post.getId(), comment.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(COMMENT_WRITER_MISMATCH.getMessage()));
        }
    }
}
