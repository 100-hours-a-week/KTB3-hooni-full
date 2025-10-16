package kakaotech.community.domain.comment;

import kakaotech.community.global.exception.CommentException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_COMMENT_CURSOR;

@Repository
public class LocalCommentRepository implements CommentRepository {
    private static final int PAGE_SIZE = 20;

    private final Map<Long, Comment> commentDatabase = new ConcurrentHashMap<>();
    private final Map<Long, List<Comment>> commentsByPostId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public synchronized Comment save(Comment comment) {
        if (comment.getId() != null) {
            commentDatabase.put(comment.getId(), comment);

            List<Comment> comments = commentsByPostId.get(comment.getPostId());
            int index = comments.indexOf(comment);
            Comment preComment = comments.get(index);
            comments.set(index, preComment.update(comment));
        }

        comment.assignId(idGenerator.getAndIncrement());
        commentDatabase.put(comment.getId(), comment);
        commentsByPostId.computeIfAbsent(comment.getPostId(), k -> new LinkedList<>()).addFirst(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(commentDatabase.get(id));
    }

    @Override
    public void delete(Comment comment) {
        commentDatabase.remove(comment.getId());
        List<Comment> comments = commentsByPostId.get(comment.getPostId());
        comments.remove(comment);
    }

    @Override
    public List<Comment> findByPostIdAndCursor(Long postId, int cursor) {
        List<Comment> comments = commentsByPostId.getOrDefault(postId, List.of());

        if (comments.isEmpty()) {
            return List.of();
        }

        int size = comments.size();

        int startIndex;
        if (cursor == 0) {
            startIndex = 0;
        } else {
            int foundIndex = -1;
            for (int i = 0; i < size; i++) {
                if (comments.get(i).getId() == cursor) {
                    foundIndex = i;
                    break;
                }
            }

            if (foundIndex == -1) { // 잘못된 cursor로 판단
                throw new CommentException(INVALID_COMMENT_CURSOR);
            }
            startIndex = foundIndex + 1;
        }

        int endIndex = Math.min(startIndex + PAGE_SIZE, size);

        if (startIndex >= endIndex) {
            return List.of();
        }
        return new ArrayList<>(comments.subList(startIndex, endIndex));
    }

    @Override
    public boolean hasNextComment(Long postId, Comment comment) {
        List<Comment> comments = commentsByPostId.get(postId);
        if (comments.isEmpty()) {
            return false;
        }

        return !comments.getLast().equals(comment);
    }
}
