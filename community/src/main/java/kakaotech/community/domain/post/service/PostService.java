package kakaotech.community.domain.post.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final ImageService imageService;

    private final PostRepository postRepository;

    public PostResponse.Detail create(Long userId, String title, String content, MultipartFile image) {
        User user = userService.findById(userId);
        UUID imageId = imageService.save(image);

        Post post = postRepository.save(new Post(userId, title, content, imageId));

        return toDetail(user, post);
    }

    private PostResponse.Detail toDetail(User user, Post post) {
        return new PostResponse.Detail(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                user.getId(),
                user.getNickname(),
                user.getProfileImageId(),
                post.getImageId(),
                post.getLikeCount(),
                post.getViewCount(),
                post.getCreatedAt()
        );
    }
}
