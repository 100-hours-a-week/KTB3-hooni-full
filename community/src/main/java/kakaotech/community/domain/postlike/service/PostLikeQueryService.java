package kakaotech.community.domain.postlike.service;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.postlike.PostLikeRepository;
import kakaotech.community.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeQueryService {
    private final PostLikeRepository postLikeRepository;

    public boolean isLiked(Post post, User user) {
        return postLikeRepository.existsByPostAndUser(post, user);
    }
}
