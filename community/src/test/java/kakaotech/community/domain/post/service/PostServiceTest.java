package kakaotech.community.domain.post.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.post.LocalPostRepository;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.domain.user.LocalUserRepository;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @MockitoBean
    private ImageService imageService;

    @Autowired
    private PostRepository postRepository;

    // 단순 DB 초기화를 위한 의존성... 없애야 함..
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDB() {
        ((LocalPostRepository) postRepository).clear();
        ((LocalUserRepository) userRepository).clear();
    }

    @Test
    void 게시글_생성_성공() {
        // given
        MultipartFile image = Mockito.mock(MultipartFile.class);
        UUID imageId = UUID.randomUUID();

        userService.join("email@email.com", "@ASasd1234", "hello", image);

        when(imageService.save(image)).thenReturn(imageId);

        // when then
        PostResponse.Detail result = postService.create(1L, "title", "content", image);
        assertThat(result).isNotNull();
        assertThat(result.writerId()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("title");
    }

    @Test
    void 게시글_조회_사이즈보다_작은_개수() {
        // given
        MultipartFile image = Mockito.mock(MultipartFile.class);
        UUID imageId = UUID.randomUUID();

        userService.join("email@email.com", "@ASasd1234", "hello", image);

        when(imageService.save(image)).thenReturn(imageId);

        createPost(image, 10);

        // when then
        PostResponse.Summaries result = postService.getPostsByPaging(0);

        assertThat(result.summaries().size()).isEqualTo(10);
        assertThat(result.paging().size()).isEqualTo(20);
        assertThat(result.paging().totalPages()).isEqualTo(1);
        assertThat(result.summaries().getFirst().createdAt()).isAfter(result.summaries().getLast().createdAt());
    }

    @Test
    void 게시글_조회_사이즈보다_많은_개수() {
        // given
        MultipartFile image = Mockito.mock(MultipartFile.class);
        UUID imageId = UUID.randomUUID();

        userService.join("email@email.com", "@ASasd1234", "hello", image);

        when(imageService.save(image)).thenReturn(imageId);

        createPost(image, 50);

        // then when
        PostResponse.Summaries result = postService.getPostsByPaging(1);

        assertThat(result.summaries().size()).isEqualTo(20);
        assertThat(result.paging().size()).isEqualTo(20);
        assertThat(result.paging().totalPages()).isEqualTo(3);
    }

    @Test
    void 게시글_조회_게시글이_없는_마지막_페이지() {
        // given
        MultipartFile image = Mockito.mock(MultipartFile.class);
        UUID imageId = UUID.randomUUID();

        userService.join("email@email.com", "@ASasd1234", "hello", image);

        when(imageService.save(image)).thenReturn(imageId);

        createPost(image, 40);

        // when then
        PostResponse.Summaries result = postService.getPostsByPaging(2);

        assertThat(result.summaries().size()).isEqualTo(0);
        assertThat(result.paging().totalPages()).isEqualTo(2);
        assertThat(result.paging().totalSizes()).isEqualTo(40);
    }

    private void createPost(MultipartFile image, int size) {
        for (int i = 0; i < size; i++) {
            postService.create(1L, "title" + i, "content" + i, image);
        }
    }
}
