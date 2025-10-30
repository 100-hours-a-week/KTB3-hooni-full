package kakaotech.community.domain.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kakaotech.community.domain.user.User;
import kakaotech.community.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image_id")
    private UUID imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "comment_count")
    private Long commentCount;

    @Column(name = "view_count")
    private Long viewCount;

    public Post(
            User writer,
            String title,
            String content,
            UUID imageId
    ) {
        this.writer = writer;
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
        return Objects.equals(writer.getId(), userId);
    }

    public Post update(String title, String content, UUID imageId) {
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        return this;
    }

    public Long getWriterId() {
        return writer.getId();
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
