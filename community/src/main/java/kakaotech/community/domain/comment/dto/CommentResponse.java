package kakaotech.community.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CommentResponse {

    public record Key(Long commentId) {}

    public record Details(
            Long postId,
            List<Detail> details,
            Paging paging
    ) {
    }

    public record Detail(
            Long commentId,
            Long writerId,
            UUID writerProfileImage,
            String writerName,
            String content,
            LocalDateTime createdAt
    ) {
    }

    public record Paging(
            Long nextCursor,
            boolean hasNext
    ) {
    }
}
