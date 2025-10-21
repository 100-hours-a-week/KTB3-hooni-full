package kakaotech.community.domain.comment.service;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.comment.CommentRepository;
import kakaotech.community.domain.comment.dto.CommentResponse;
import kakaotech.community.domain.post.service.PostService;
import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.service.UserService;
import kakaotech.community.global.exception.CommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kakaotech.community.global.exception.code.ExceptionCode.COMMENT_NOT_FOUND;
import static kakaotech.community.global.exception.code.ExceptionCode.COMMENT_WRITER_MISMATCH;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final UserService userService;

    private final CommentRepository commentRepository;

    public CommentResponse.Key create(Long userId, Long postId, String content) {
        postService.validatePost(postId);

        Comment comment = commentRepository.save(new Comment(userId, postId, content));
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

    public CommentResponse.Details getCommentsByCursor(Long postId, int cursor) {
        postService.validatePost(postId);

        List<Comment> comments = commentRepository.findByPostIdAndCursor(postId, cursor);
        boolean hasNext = commentRepository.hasNextComment(postId, comments.getLast());

        return new CommentResponse.Details(
                postId,
                comments.stream()
                        .map(comment -> {
                                    User user = userService.findById(comment.getWriterId());

                                    return CommentMapper.toDetail(comment, user);
                                }
                        ).toList(),
                new CommentResponse.Paging(comments.getLast().getId(), hasNext)
        );
    }
}
