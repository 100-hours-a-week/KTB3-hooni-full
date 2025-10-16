package kakaotech.community.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {

    public record Write(@NotBlank String content) {}
}
