package kakaotech.community.domain.post;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public Optional<Post> findById(Long id) {
       return Optional.ofNullable(postDatabase.get(id));
    }

    public List<Post> findPostsByPaging(int page) {
        List<Post> snapshot = new ArrayList<>(postDatabase.values());
        sort(snapshot);

        int totalSize = snapshot.size();
        int fromIndex = Math.min(page * 20, totalSize);
        int toIndex = Math.min(fromIndex + 20, totalSize);

        return (fromIndex < toIndex) ? snapshot.subList(fromIndex, toIndex) : List.of();
    }

    public void deleteById(Long id) {
        postDatabase.remove(id);
    }

    public int size() {
        return postDatabase.size();
    }

    public void clear() {
        postDatabase.clear();
    }

    private void sort(List<Post> list) {
        list.sort((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));
    }
}
