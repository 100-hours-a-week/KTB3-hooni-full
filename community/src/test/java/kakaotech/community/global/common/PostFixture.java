package kakaotech.community.global.common;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class PostFixture {

    private PostFixture() {
    }

    static Post one(User user, UUID imageId) {
        return new Post(user, "title", "content", imageId);
    }

    static Post oneWithoutImage(User user) {
        return one(user, null);
    }

    static List<Post> many(User user, int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> oneWithoutImage(user))
                .toList();
    }
}
