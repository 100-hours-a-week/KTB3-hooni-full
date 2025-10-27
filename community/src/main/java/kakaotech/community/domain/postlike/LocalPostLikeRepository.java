package kakaotech.community.domain.postlike;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LocalPostLikeRepository implements PostLikeRepository {
    // key 는 post id, value는 이 post에 좋아요를 누른 user id
    private final Map<Long, Set<Long>> postLikeDatabase = new ConcurrentHashMap<>();

    @Override
    public synchronized PostLike save(PostLike postLike) {
        Set<Long> likedDatabase = postLikeDatabase.getOrDefault(postLike.getPostId(), Collections.synchronizedSet(new HashSet<>()));
        likedDatabase.add(postLike.getUserId());
        postLikeDatabase.put(postLike.getPostId(), likedDatabase);

        return postLike;
    }

    @Override
    public synchronized void delete(Long postId, Long userId) {
        if (!postLikeDatabase.containsKey(postId)) {
            return;
        }

        postLikeDatabase.get(postId).remove(userId);
    }

    @Override
    public boolean existsByPostIdAndUserId(Long postId, Long userId) {
        return postLikeDatabase.containsKey(postId) && postLikeDatabase.get(postId).contains(userId);
    }

    public void clear() {
        postLikeDatabase.clear();
    }
}
