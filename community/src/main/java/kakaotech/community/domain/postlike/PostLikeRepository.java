package kakaotech.community.domain.postlike;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;

public interface PostLikeRepository {
    PostLike save(PostLike postLike);

    void delete(Post post, User user);

    boolean existsByPostAndUser(Post post, User user);
}
