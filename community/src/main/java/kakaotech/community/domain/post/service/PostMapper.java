package kakaotech.community.domain.post.service;

import kakaotech.community.domain.post.Post;
import kakaotech.community.domain.post.dto.PostResponse;
import kakaotech.community.domain.user.User;

public class PostMapper {

    static PostResponse.Detail toDetail(User user, Post post, boolean liked) {
        return new PostResponse.Detail(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                user.getId(),
                user.getNickname(),
                user.getProfileImage(),
                post.getImageId() == null ? null : post.getImageId(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getViewCount(),
                post.getCreatedAt(),
                liked
        );
    }

    static PostResponse.Summary toSummary(Post post, User user) {
        return new PostResponse.Summary(
                post.getId(),
                post.getTitle(),
                user.getId(),
                user.getNickname(),
                user.getProfileImage(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getViewCount(),
                post.getCreatedAt()
        );
    }
}
