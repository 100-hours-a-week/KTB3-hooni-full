package kakaotech.community.domain.comment.service;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.comment.CommentDetailProjection;
import kakaotech.community.domain.comment.CommentRepository;
import kakaotech.community.domain.comment.dto.CommentResponse;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.service.PostService;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.service.UserService;
import kakaotech.community.global.exception.CommentException;
import kakaotech.community.global.page.CursorResult;
import kakaotech.community.global.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kakaotech.community.global.exception.code.ExceptionCode.COMMENT_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.COMMENT_WRITER_MISMATCH;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final UserService userService;

    private final CommentRepository commentRepository;

    public CommentResponse.Key create(Long userId, Long postId, String content) {
        Post post = postService.findById(postId);
        User user = userService.findById(userId);

        Comment comment = commentRepository.save(new Comment(user, post, content));
        return new CommentResponse.Key(comment.getId());
    }

    public CommentResponse.Key update(Long userId, Long postId, Long commentId, String content) {
        postService.validatePost(postId);

        Comment preComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        if (!preComment.isWrittenBy(userId)) {
            throw new CommentException(COMMENT_WRITER_MISMATCH);
        }

        Comment newComment = commentRepository.save(preComment.update(content));
        return new CommentResponse.Key(newComment.getId());
    }

    public void delete(Long userId, Long postId, Long commentId) {
        postService.validatePost(postId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        if (!comment.isWrittenBy(userId)) {
            throw new CommentException(COMMENT_WRITER_MISMATCH);
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public CommentResponse.Details getCommentsByCursor(Long postId, int cursor) {
        postService.validatePost(postId);

        PageQuery pageQuery = PageQuery.cursor();

        CursorResult<CommentDetailProjection> result = commentRepository.findByPostIdAndCursor(postId, cursor, pageQuery);
        Long nextCursor = result.elements().isEmpty() ? 0L : result.elements().getLast().commentId();

        return new CommentResponse.Details(
                postId,
                result.elements().stream()
                        .map(element ->
                             new CommentResponse.Detail(
                                    element.commentId(), element.writerId(),
                                    element.writerProfileImage(), element.writerName(),
                                    element.content(), element.createdAt()
                            )
                        ).toList(),
                new CommentResponse.Paging(nextCursor, result.hasNext())
        );
    }
}
