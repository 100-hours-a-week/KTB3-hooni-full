package kakaotech.community.domain.comment;

import kakaotech.community.global.page.CursorResult;
import kakaotech.community.global.page.PageQuery;

import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    void delete(Comment comment);

    CursorResult<CommentDetailProjection> findByPostIdAndCursor(Long postId, int cursor, PageQuery pageQuery);
}
