package kakaotech.community.domain.postlike.service;

import kakaotech.community.domain.postlike.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeQueryService {
    private final PostLikeRepository postLikeRepository;

    public boolean isLiked(Long postId, Long userId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }
}
