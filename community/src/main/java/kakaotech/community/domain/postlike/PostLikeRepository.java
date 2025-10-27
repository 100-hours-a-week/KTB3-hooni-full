package kakaotech.community.domain.postlike;

public interface PostLikeRepository {
    PostLike save(PostLike postLike);

    void delete(Long postId, Long userId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
