package kakaotech.community.global.common;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.user.User;

import java.util.List;
import java.util.stream.IntStream;

public class CommentFixture {

    private CommentFixture() {
    }

    static Comment one(Post post, User user) {
        return new Comment(user, post, "content something");
    }

    static List<Comment> many(Post post, User user, int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> one(post, user))
                .toList();
    }
}
