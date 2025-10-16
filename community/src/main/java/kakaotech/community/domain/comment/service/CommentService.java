package kakaotech.community.domain.comment.service;

import kakaotech.community.domain.comment.Comment;
import kakaotech.community.domain.comment.CommentRepository;
import kakaotech.community.domain.comment.dto.CommentResponse;
import kakaotech.community.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;

    private final CommentRepository commentRepository;

    public CommentResponse.Key create(Long userId, Long postId, String content) {
        postService.validatePost(postId);

        Comment comment = commentRepository.save(new Comment(userId, postId, content));
        return new CommentResponse.Key(comment.getId());
    }
}
