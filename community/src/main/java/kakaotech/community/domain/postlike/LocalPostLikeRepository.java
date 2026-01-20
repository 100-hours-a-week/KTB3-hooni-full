package kakaotech.community.domain.postlike;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;
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
    public synchronized void delete(Post post, User user) {
        if (!postLikeDatabase.containsKey(post.getId())) {
            return;
        }

        postLikeDatabase.get(post.getId()).remove(user.getId());
    }

    @Override
    public boolean existsByPostAndUser(Post post, User user) {
        return postLikeDatabase.containsKey(post.getId()) && postLikeDatabase.get(post.getId()).contains(user.getId());
    }

    public void clear() {
        postLikeDatabase.clear();
    }
}
