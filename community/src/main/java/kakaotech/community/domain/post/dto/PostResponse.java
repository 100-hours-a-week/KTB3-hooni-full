package kakaotech.community.domain.post.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class PostResponse {

    public record Detail(
            Long postId,
            String title,
            String content,
            Long writerId,
            String writerName,
            UUID writerProfileImage,
            UUID image,
            Long likeCount,
            Long viewCount,
            LocalDateTime createdAt
    ) {
    }
}
