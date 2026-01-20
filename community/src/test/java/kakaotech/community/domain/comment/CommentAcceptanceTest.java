package kakaotech.community.domain.comment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakaotech.community.domain.comment.dto.CommentRequest;
import kakaotech.community.domain.comment.dto.CommentResponse;
import kakaotech.community.domain.comment.service.CommentService;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.user.User;
import kakaotech.community.global.common.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private CommentService commentService;

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

    @Nested
    class 댓글_삭제_테스트 {
        private final String url = "/posts/{postId}/comments/{commentId}";

        @Test
        void 삭제_성공() throws Exception {
            // given
            Comment comment = fixtures.댓글_생성(post, user);
            assertThat(commentRepository.findById(comment.getId()).isPresent()).isTrue();

            // when
            mockMvc.perform(
                    delete(url, post.getId(), comment.getId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
                    .andExpect(status().isNoContent());

            // then
            assertThat(commentRepository.findById(comment.getId()).isPresent()).isFalse();
        }

        @Test
        void 삭제_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongPostId = 9999L;
            assertThat(postRepository.findById(wrongPostId).isPresent()).isFalse();

            Comment comment = fixtures.댓글_생성(post, user);
            assertThat(commentRepository.findById(comment.getId()).isPresent()).isTrue();

            // when
            mockMvc.perform(
                    delete(url, wrongPostId, comment.getId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));

            // then
            assertThat(commentRepository.findById(comment.getId()).isPresent()).isTrue();
        }

        @Test
        void 삭제_실패_잘못된_댓글_ID() throws Exception {
            // given
            Long wrongCommentId = 9999L;
            assertThat(commentRepository.findById(wrongCommentId).isPresent()).isFalse();

            Comment comment = fixtures.댓글_생성(post, user);

            // when
            mockMvc.perform(
                            delete(url, post.getId(), wrongCommentId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(COMMENT_NOT_FOUND.getMessage()));

            // then
            assertThat(commentRepository.findById(comment.getId()).isPresent()).isTrue();
        }

        @Test
        void 삭제_실패_내가_작성한_댓글이_아님() throws Exception {
            // given
            Comment comment = fixtures.댓글_생성(post, another);

            // when
            mockMvc.perform(
                            delete(url, post.getId(), comment.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(COMMENT_WRITER_MISMATCH.getMessage()));

            // then
            assertThat(commentRepository.findById(comment.getId()).isPresent()).isTrue();
        }
    }

    @Nested
    class 커서_기반_댓글_조회_테스트 {
        private final String url = "/posts/{postId}/comments";

        private final int PAGE_SIZE = 20;

        @Test
        void 댓글이_하나도_없을때() throws Exception {

            // when
            MvcResult result = mockMvc.perform(
                            get(url, post.getId())
                                    .queryParam("cursor", "0")
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("details").isEmpty()).isTrue()
            );
        }

        @Test
        @DisplayName("커서가 초기값인 0이면서 댓글 수가 페이지 크기인 20보다 작을 경우")
        void 페이지보다_적은_양의_댓글과_커서_초기값() throws Exception {
            // given
            int size = 10;
            fixtures.댓글_여러개_생성(post, another, size);

            // when
            MvcResult result = mockMvc.perform(
                            get(url, post.getId())
                                    .queryParam("cursor", "0")
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("details").size()).isEqualTo(size),
                    () -> assertThat(root.get("paging").get("hasNext").asBoolean()).isFalse()
            );
        }

        @Test
        @DisplayName("커서가 초기값인 0이면서 댓글 수가 페이지 크기인 20보다 큰 경우")
        void 페이지보다_많은_양의_댓글과_커서_초기값() throws Exception {
            // given
            int size = 30;
            fixtures.댓글_여러개_생성(post, another, size);

            // when
            MvcResult result = mockMvc.perform(
                            get(url, post.getId())
                                    .queryParam("cursor", "0")
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("details").size()).isEqualTo(PAGE_SIZE),
                    () -> assertThat(root.get("paging").get("nextCursor").asLong())
                                .isEqualTo(root.get("details").get(PAGE_SIZE - 1).get("commentId").asLong()),
                    () -> assertThat(root.get("paging").get("hasNext").asBoolean()).isTrue()
            );
        }

        @Test
        @DisplayName("커서가 중간 값이면서 다음 페이지에도 댓글이 남아있을 경우")
        void 중간_커서_값과_충분히_많은_댓글() throws Exception {
            // given
            int size = 80;
            fixtures.댓글_여러개_생성(post, another, size);

            var pre = commentService.getCommentsByCursor(post.getId(), 0);
            Long cursor = pre.paging().nextCursor();

            // when
            MvcResult result = mockMvc.perform(
                            get(url, post.getId())
                                    .queryParam("cursor", String.valueOf(cursor))
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("details").size()).isEqualTo(PAGE_SIZE),
                    () -> assertThat(root.get("paging").get("hasNext").asBoolean()).isTrue(),
                    () -> assertThat(root.get("paging").get("nextCursor").asLong())
                            .isEqualTo(root.get("details").get(PAGE_SIZE - 1).get("commentId").asLong())
            );
        }

        @Test
        @DisplayName("커서가 마지막 페이지를 보여주는 경우")
        void 마지막_커서_값과_딱_맞아떨어지는_댓글() throws Exception {
            // given
            int size = 40;
            fixtures.댓글_여러개_생성(post, another, size);

            var pre = commentService.getCommentsByCursor(post.getId(), 0);
            Long cursor = pre.paging().nextCursor();

            // when
            MvcResult result = mockMvc.perform(
                            get(url, post.getId())
                                    .queryParam("cursor", String.valueOf(cursor))
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            String content = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(content);

            assertAll(
                    () -> assertThat(root.get("postId").asLong()).isEqualTo(post.getId()),
                    () -> assertThat(root.get("details").size()).isEqualTo(PAGE_SIZE),
                    () -> assertThat(root.get("paging").get("hasNext").asBoolean()).isFalse()
            );
        }

        @Test
        void 조회_실패_잘못된_커서_값() throws Exception {
            // given
            int wrongCursor = -1;

            // when then
            mockMvc.perform(
                            get(url, post.getId())
                                    .queryParam("cursor", String.valueOf(wrongCursor))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 조회_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongPostId = 9999L;
            assertThat(postRepository.findById(wrongPostId).isPresent()).isFalse();

            // when
            mockMvc.perform(
                    get(url, wrongPostId)
                            .queryParam("cursor", "0")
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
        }
    }
}
