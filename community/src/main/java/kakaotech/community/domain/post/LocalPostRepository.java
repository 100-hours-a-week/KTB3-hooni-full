package kakaotech.community.domain.post;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LocalPostRepository implements PostRepository {
    private final Map<Long, Post> postDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public synchronized Post save(Post post) {
        if (post.getId() != null) {
            postDatabase.put(post.getId(), post);
            return post;
        }

        post.assignId(idGenerator.getAndIncrement());
        postDatabase.put(post.getId(), post);
        return post;
    }

    public void clear() {
        postDatabase.clear();
    }
}
