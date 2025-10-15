package kakaotech.community.domain.post.controller;

import jakarta.validation.Valid;
import kakaotech.community.domain.post.dto.PostRequest;
import kakaotech.community.domain.post.service.PostService;
import kakaotech.community.global.auth.annotation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> create(@Authenticated Long userId,
                                    @Valid @ModelAttribute PostRequest.Create request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.create(userId, request.title(), request.content(), request.image()));
    }
}
