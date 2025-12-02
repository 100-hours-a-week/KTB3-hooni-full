package kakaotech.community.global.common;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;

import java.util.UUID;

public class PostFixture {

    private PostFixture() {
    }

    static Post one(User user, UUID imageId) {
        return new Post(user, "title", "content", imageId);
    }

    static Post oneWithoutImage(User user) {
        return one(user, null);
    }
}
