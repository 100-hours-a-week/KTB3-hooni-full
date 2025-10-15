package kakaotech.community.domain.postlike.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.domain.post.service.PostService;
import kakaotech.community.domain.postlike.PostLikeRepository;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostLikeServiceTest {
    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @MockitoBean
    private ImageService imageService;

    @Test
    void 게시글에_좋아요() {
        // given
        userService.join("email@email.com", "@Aasd1234", "nickname", mock(MultipartFile.class));
        PostResponse.Detail detail = postService.create(1L, "title", "content", mock(MultipartFile.class));

        when(imageService.save(mock(MultipartFile.class))).thenReturn(mock(UUID.class));
        // when
        postLikeService.like(detail.postId(), 2L);

        // then
        assertThat(postService.getPost(detail.postId()).likeCount()).isEqualTo(1L);
    }
}
