package kakaotech.community.domain.post;

import kakaotech.community.domain.user.UserRepository;
import kakaotech.community.global.page.PageQuery;
import kakaotech.community.global.page.PageResult;
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

    public PageResult<PostSummaryProjection> findPostsByPaging(PageQuery pageQuery) {
        int pageNum = pageQuery.pageNum();
        int pageSize = pageQuery.pageSize();
        List<Post> snapshot = new ArrayList<>(postDatabase.values());
        sort(snapshot);

        int totalSize = snapshot.size();
        int fromIndex = Math.min(pageNum * pageSize, totalSize);
        int toIndex = Math.min(fromIndex + pageSize, totalSize);

        if (fromIndex < toIndex) {
            return new PageResult<>(snapshot.subList(fromIndex, toIndex).stream()
                    .map(post ->
                            new PostSummaryProjection(
                                    post.getId(), post.getTitle(), post.getWriterId(),
                                    post.getWriter().getNickname(), post.getWriter().getProfileImage(),
                                    post.getLikeCount(), post.getCommentCount(), post.getViewCount(), post.getCreatedAt()
                            )
                    ).toList(),
                    pageNum, pageSize, calculateTotalPage(totalSize, pageSize), totalSize);
        }

        return new PageResult<>(List.of(), pageNum, pageSize, calculateTotalPage(totalSize, pageSize), totalSize);
    }

    private int calculateTotalPage(int totalSize, int pageSize) {
        return (totalSize % pageSize == 0) ? totalSize / pageSize : (totalSize / pageSize) + 1;
    }

    public void deleteById(Long id) {
        postDatabase.remove(id);
    }

    public boolean existsById(Long id) {
        return postDatabase.containsKey(id);
    }

    public void clear() {
        postDatabase.clear();
    }

    private void sort(List<Post> list) {
        list.sort((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));
    }
}
