package kakaotech.community.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;
import kakaotech.community.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "content)")
    private String content;

    public Comment(User writer, Post post, String content) {
        this.writer = writer;
        this.post = post;
        this.content = content;
    }

    Comment update(Comment comment) {
        this.content = comment.getContent();
        return this;
    }

    void assignId(Long id) {
        this.id = id;
    }

    public boolean isWrittenBy(Long userId) {
        return Objects.equals(writer.getId(), userId);
    }

    public Comment update(String content) {
        this.content = content;
        return this;
    }

    public Long getWriterId() {
        return this.writer.getId();
    }

    public Long getPostId() {
        return this.post.getId();
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
