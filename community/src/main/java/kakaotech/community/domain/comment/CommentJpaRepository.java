package kakaotech.community.domain.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    @Query("""
        SELECT new kakaotech.community.domain.comment.CommentDetailProjection(
                c.id, u.id, u.profileImage,
                u.nickname, c.content, c.createdAt
                )
        FROM Comment c LEFT JOIN User u ON c.writer.id = u.id
        WHERE c.post.id =  :postId AND (:cursor = 0 OR :cursor < c.id)
        """)
    Slice<CommentDetailProjection> findCommentDetailsByPostId(Long postId, int cursor, Pageable pageable);
}
