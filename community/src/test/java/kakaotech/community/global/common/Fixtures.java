package kakaotech.community.global.common;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.comment.CommentJpaRepository;
import kakaotech.community.domain.comment.CommentRepository;
import kakaotech.community.domain.image.ImageStorage;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostJpaRepository;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.domain.user.port.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public final class Fixtures {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private ImageStorage imageStorage;

    @Autowired
    private PostJpaRepository postRepository;

    @Autowired
    private CommentJpaRepository commentRepository;

    // 사용자
    public String 토큰_발행(User user) {
        return tokenGenerator.login(user.getId()).getAccessToken();
    }

    public String 리프레시_토큰_발행(User user) {
        return tokenGenerator.login(user.getId()).getRefreshToken();
    }

    public User 사용자_생성() {
        UUID imageId = 이미지_생성();
        return userRepository.save(UserFixture.one(imageId));
    }

    public User 다른_사용자_생성() {
        UUID imageId = 이미지_생성();
        return userRepository.save(UserFixture.another(imageId));
    }

    // 이미지
    public UUID 이미지_생성() {
        UUID uuid = UUID.randomUUID();
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "something image".getBytes()
        );

        return imageStorage.upload(uuid, image);
    }

    // 게시글
    public Post 게시글_생성(User user) {
        UUID imageId = 이미지_생성();
        return postRepository.save(PostFixture.one(user, imageId));
    }

    public Post 게시글_생성_이미지_없는(User user) {
        return postRepository.save(PostFixture.oneWithoutImage(user));
    }

    public List<Post> 게시글_여러개_생성(User user, int size) {
        return postRepository.saveAll(PostFixture.many(user, size));
    }


    // 댓글
    public Comment 댓글_생성(Post post, User user) {
        return commentRepository.save(CommentFixture.one(post, user));
    }

    public List<Comment> 댓글_여러개_생성(Post post, User user, int size) {
        return commentRepository.saveAll(CommentFixture.many(post, user, size));
    }
}
