package kakaotech.community.domain.postlike;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.service.PostService;
import kakaotech.community.domain.postlike.service.PostLikeQueryService;
import kakaotech.community.domain.postlike.service.PostLikeService;
import kakaotech.community.domain.user.User;
import kakaotech.community.global.common.Fixtures;
import kakaotech.community.global.exception.PostException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static kakaotech.community.global.exception.code.ExceptionCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostLikeAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Fixtures fixtures;

    @Autowired
    private PostLikeQueryService postLikeQueryService;

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostService postService;

    private User user;
    private String accessToken;

    private User someone;

    private Post post;

    @BeforeEach
    void setUp() {
        user = fixtures.사용자_생성();
        accessToken = fixtures.토큰_발행(user);

        someone = fixtures.다른_사용자_생성();
        post = fixtures.게시글_생성(someone);
    }

    @Nested
    class 좋아요_테스트 {
        private final String url = "/posts/{postId}/like";

        @Test
        void 게시글에_좋아요_성공() throws Exception {
            // given
            Long preLikeCount = postService.findById(post.getId()).getLikeCount();

            assertThat(postLikeQueryService.isLiked(post, user)).isFalse();

            // when
            mockMvc.perform(
                            put(url, post.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result").value("success"));

            // then
            assertAll(
                    () -> assertThat(postLikeQueryService.isLiked(post, user)).isTrue(),
                    () -> assertThat(postService.findById(post.getId()).getLikeCount()).isGreaterThan(preLikeCount)
            );
        }

        @Test
        void 좋아요_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongPostId = 9999L;
            assertThatThrownBy(() -> postService.findById(wrongPostId))
                    .isInstanceOf(PostException.class);

            // when then
            mockMvc.perform(
                    put(url, wrongPostId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
        }

        @Test
        void 이미_좋아요를_누른_게시글예_다시_좋아요_요청() throws Exception {
            // given
            postLikeService.like(post.getId(), user.getId());
            assertThat(postLikeQueryService.isLiked(post, user)).isTrue();

            Long preLikeCount = postService.findById(post.getId()).getLikeCount();

            // when
            mockMvc.perform(
                    put(url, post.getId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
                    .andExpect(status().isOk());

            // then
            assertAll(
                    () -> assertThat(postLikeQueryService.isLiked(post, user)).isTrue(),
                    () -> assertThat(postService.findById(post.getId()).getLikeCount()).isEqualTo(preLikeCount)
            );
        }
    }

    @Nested
    class 좋아요_취소_테스트 {
        private final String url = "/posts/{postId}/like";

        @Test
        void 좋아요_취소_성공() throws Exception {
            // given
            postLikeService.like(post.getId(), user.getId());
            assertThat(postLikeQueryService.isLiked(post, user)).isTrue();

            Long preLikeCount = postService.findById(post.getId()).getLikeCount();

            // when
            mockMvc.perform(
                    delete(url, post.getId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result").value("success"));

            // then
            assertAll(
                    () -> assertThat(postLikeQueryService.isLiked(post, user)).isFalse(),
                    () -> assertThat(postService.findById(post.getId()).getLikeCount()).isLessThan(preLikeCount)
            );
        }

        @Test
        void 좋아요_실패_잘못된_게시글_ID() throws Exception {
            // given
            Long wrongPostId = 9999L;
            assertThatThrownBy(() -> postService.findById(wrongPostId))
                    .isInstanceOf(PostException.class);

            // when then
            mockMvc.perform(
                    delete(url, wrongPostId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
        }

        @Test
        void 좋아요를_누르지않은_게시글에_좋아요_취소_요청() throws Exception {
            // given
            assertThat(postLikeQueryService.isLiked(post, user)).isFalse();
            Long preLikeCount = postService.findById(post.getId()).getLikeCount();

            // when
            mockMvc.perform(
                            delete(url, post.getId())
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result").value("success"));

            // then
            assertAll(
                    () -> assertThat(postLikeQueryService.isLiked(post, user)).isFalse(),
                    () -> assertThat(postService.findById(post.getId()).getLikeCount()).isEqualTo(preLikeCount)
            );
        }
    }
}
