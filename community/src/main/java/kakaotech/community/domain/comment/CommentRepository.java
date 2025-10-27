package kakaotech.community.domain.comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    void delete(Comment comment);

    List<Comment> findByPostIdAndCursor(Long postId, int cursor);

    boolean hasNextComment(Long postId, Comment comment);
}
