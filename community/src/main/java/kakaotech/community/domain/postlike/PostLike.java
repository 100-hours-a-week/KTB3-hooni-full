package kakaotech.community.domain.postlike;

import kakaotech.community.global.entity.BaseEntity;
import lombok.Getter;


@Getter
public class PostLike extends BaseEntity {
    private Long postId;
    private Long userId;

    public PostLike(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
