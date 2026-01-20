package kakaotech.community.infrastructure.jpa;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.comment.CommentDetailProjection;
import kakaotech.community.domain.comment.CommentJpaRepository;
import kakaotech.community.domain.comment.CommentRepository;
import kakaotech.community.global.page.CursorResult;
import kakaotech.community.global.page.PageQuery;
import kakaotech.community.global.page.SortSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
@RequiredArgsConstructor
public class CommentJpaAdapter implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id);
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public CursorResult<CommentDetailProjection> findByPostIdAndCursor(Long postId, int cursor, PageQuery pageQuery) {
        Sort.Direction direction = (pageQuery.sort().direction() == SortSpec.Direction.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageQuery.pageNum(), pageQuery.pageSize(), Sort.by(direction, pageQuery.sort().property(), "id"));

        Slice<CommentDetailProjection> results = commentJpaRepository.findCommentDetailsByPostId(postId, cursor, pageable);

        return new CursorResult<>(results.getContent(), results.hasNext());
    }
}
