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

import java.util.List;
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

    public PostResponse.Summaries getPostsByPaging(int page) {
        List<Post> posts = postRepository.findPostsByPaging(page);

        int totalSize = postRepository.size();

        return new PostResponse.Summaries(
                posts.stream()
                        .map(post -> {
                                    User user = userService.findById(post.getWriterId());

                                    return new PostResponse.Summary(
                                            post.getId(),
                                            post.getTitle(),
                                            user.getId(),
                                            user.getNickname(),
                                            user.getProfileImageId(),
                                            post.getLikeCount(),
                                            post.getCommentCount(),
                                            post.getViewCount(),
                                            post.getCreatedAt()
                                    );
                                }
                        ).toList(),
                // totalSize 는 20개마다 1페이지씩 늘어남. (20개 -> 총 페이지 1, 39개 -> 1, 40개 -> 2 ...)
                new PostResponse.Paging(page, 20, (totalSize % 20 == 0) ? totalSize / 20 : (totalSize / 20) + 1, totalSize)
        );
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
