package kakaotech.community.domain.comment.controller;

import jakarta.validation.Valid;
import kakaotech.community.domain.comment.dto.CommentRequest;
import kakaotech.community.domain.comment.dto.CommentResponse;
import kakaotech.community.domain.comment.service.CommentService;
import kakaotech.community.global.auth.annotation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
                                                      @Valid @RequestBody CommentRequest.Write request) {
        return ResponseEntity.ok(commentService.create(userId, postId, request.content()));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse.Key> update(@Authenticated Long userId,
                                                      @PathVariable Long postId,
                                                      @PathVariable Long commentId,
                                                      @Valid @RequestBody CommentRequest.Write request) {
        return ResponseEntity.ok(commentService.update(userId, postId, commentId, request.content()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@Authenticated Long userId,
                                    @PathVariable Long postId,
                                    @PathVariable Long commentId) {
        commentService.delete(userId, postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
