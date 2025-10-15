package kakaotech.community.domain.postlike.controller;

import kakaotech.community.domain.postlike.service.PostLikeService;
import kakaotech.community.global.auth.annotation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@Authenticated Long userId, @PathVariable Long postId) {
        postLikeService.like(postId, userId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
