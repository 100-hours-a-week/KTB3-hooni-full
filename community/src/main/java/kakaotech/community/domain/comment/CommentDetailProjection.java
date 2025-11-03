package kakaotech.community.domain.comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentDetailProjection(
        Long commentId,
        Long writerId,
        UUID writerProfileImage,
        String writerName,
        String content,
        LocalDateTime createdAt
) {
}
