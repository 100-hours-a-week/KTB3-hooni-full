package kakaotech.community.domain.comment.service;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.comment.dto.CommentResponse;
import kakaotech.community.domain.user.User;

public class CommentMapper {

    static CommentResponse.Detail toDetail(Comment comment, User user) {
        return new CommentResponse.Detail(
                comment.getId(),
                user.getId(),
                user.getProfileImage(),
                user.getNickname(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
