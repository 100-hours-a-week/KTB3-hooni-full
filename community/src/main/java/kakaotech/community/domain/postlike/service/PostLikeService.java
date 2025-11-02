package kakaotech.community.domain.postlike.service;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.service.PostService;
import kakaotech.community.domain.postlike.PostLike;
import kakaotech.community.domain.postlike.PostLikeRepository;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {
    private final UserService userService;
    private final PostService postService;

    private final PostLikeRepository postLikeRepository;

    public void like(Long postId, Long userId) {
        Post post = postService.findById(postId);
        User user = userService.findById(userId);

        post.liked();

        postLikeRepository.save(new PostLike(post, user));
    }

    public void unlike(Long postId, Long userId) {
        Post post = postService.findById(postId);
        User user = userService.findById(userId);

        post.unliked();

        postLikeRepository.delete(post, user);
    }
}
