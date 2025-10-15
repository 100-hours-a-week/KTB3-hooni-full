package kakaotech.community.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;
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

    public record Summary(
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

    public record Summaries(
            List<Summary> summaries,
            Paging paging
    ) {
    }

    public record Paging(
        int page,
        int size,
        int totalPages,
        int totalSizes
    ) {
    }
}
