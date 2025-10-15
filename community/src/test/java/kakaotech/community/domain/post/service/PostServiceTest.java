package kakaotech.community.domain.post.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.domain.user.service.UserService;
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
}
