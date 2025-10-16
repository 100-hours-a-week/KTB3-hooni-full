package kakaotech.community.domain.post.service;

import kakaotech.community.domain.image.service.ImageService;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.PostRepository;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.domain.postlike.service.PostLikeQueryService;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.service.UserService;
import kakaotech.community.global.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.POST_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.POST_WRITER_MISMATCH;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final ImageService imageService;
    private final PostLikeQueryService postLikeQueryService;

    private final PostRepository postRepository;

    public PostResponse.Detail create(Long userId, String title, String content, MultipartFile image) {
        User user = userService.findById(userId);
        UUID imageId = imageService.save(image);

        Post post = postRepository.save(new Post(userId, title, content, imageId));

        return toDetail(user, post);
    }

    public PostResponse.Detail getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        User writer = userService.findById(post.getWriterId());

        post.viewedOne();
        postRepository.save(post);

        return toDetail(writer, post);
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
                post.getCommentCount(),
                post.getViewCount(),
                post.getCreatedAt(),
                postLikeQueryService.isLiked(post.getId(), user.getId())
        );
    }

    public PostResponse.Detail update(Long userId, Long postId, String title, String content, MultipartFile image) {
        Post prePost = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (!prePost.isWrittenBy(userId)) {
            throw new PostException(POST_WRITER_MISMATCH);
        }

        UUID newImageId = imageService.updateImage(prePost.getImageId(), image);

        Post newPost = postRepository.save(prePost.update(title, content, newImageId));
        User user = userService.findById(userId);

        return toDetail(user, newPost);
    }

    public void delete(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (!post.isWrittenBy(userId)) {
            throw new PostException(POST_WRITER_MISMATCH);
        }

        imageService.delete(post.getImageId());
        postRepository.deleteById(postId);
    }

    public void validatePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostException(POST_NOT_FOUND);
        }
    }
}
