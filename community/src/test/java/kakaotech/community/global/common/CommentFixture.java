package kakaotech.community.global.common;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;

public class CommentFixture {

    private CommentFixture() {
    }

    static Comment one(Post post, User user) {
        return new Comment(user, post, "content something");
    }
}
