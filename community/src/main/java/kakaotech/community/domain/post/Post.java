package kakaotech.community.domain.post;

import kakaotech.community.global.entity.BaseEntity;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Post extends BaseEntity {
    private Long id;
    private String title;
    private String content;
    private UUID imageId;
    private Long writerId;
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;

    public Post(
            Long writerId,
            String title,
            String content,
            UUID imageId
    ) {
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        this.likeCount = 0L;
        this.commentCount = 0L;
        this.viewCount = 0L;
    }

    void assignId(Long id) {
        this.id = id;
    }

    public boolean isWrittenBy(Long userId) {
        return Objects.equals(this.writerId, userId);
    }

    public Post update(String title, String content, UUID imageId) {
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        return this;
    }

    public void liked() {
        this.likeCount++;
    }

    public void unliked() {
        this.likeCount--;
    }

    public void viewedOne() {
        this.viewCount++;
    }
}
