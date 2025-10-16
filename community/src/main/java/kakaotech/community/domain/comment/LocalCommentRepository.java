package kakaotech.community.domain.comment;

import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LocalCommentRepository implements CommentRepository {
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
}
