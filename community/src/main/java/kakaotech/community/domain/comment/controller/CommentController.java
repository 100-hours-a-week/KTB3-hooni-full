package kakaotech.community.domain.comment.controller;

import jakarta.validation.Valid;
import kakaotech.community.domain.comment.dto.CommentRequest;
import kakaotech.community.domain.comment.dto.CommentResponse;
import kakaotech.community.domain.comment.service.CommentService;
import kakaotech.community.global.auth.annotation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse.Key> create(@Authenticated Long userId,
                                                      @PathVariable Long postId,
                                                      @Valid @RequestBody CommentRequest.Create request) {
        return ResponseEntity.ok(commentService.create(userId, postId, request.content()));
    }
}
