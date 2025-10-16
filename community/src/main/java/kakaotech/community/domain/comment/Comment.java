package kakaotech.community.domain.comment;

import kakaotech.community.global.entity.BaseEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Comment extends BaseEntity {
    private Long id;
    private Long writerId;
    private Long postId;
    private String content;

    public Comment(Long writerId, Long postId, String content) {
        super(LocalDateTime.now());
        this.writerId = writerId;
        this.postId = postId;
        this.content = content;
    }

    Comment update(Comment comment) {
        this.content = comment.getContent();
        return this;
    }

    void assignId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Comment comment) {
            return Objects.equals(this.id, comment.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
