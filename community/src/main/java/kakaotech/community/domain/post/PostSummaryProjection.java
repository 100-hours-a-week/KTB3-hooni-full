package kakaotech.community.domain.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostSummaryProjection(
        Long postId,
        String title,
        Long writerId,
        String writerName,
        UUID writerProfileImage,
        Long likeCount,
        Long commentCount,
        Long viewCount,
        LocalDateTime createdAt
) {
}
