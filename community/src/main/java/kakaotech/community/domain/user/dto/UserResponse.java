package kakaotech.community.domain.user.dto;

import java.util.UUID;

public class UserResponse {

    public record Join(Long id) {
    }

    public record Update(
            Long userId,
            String email,
            String nickname,
            UUID profileImage
    ) {
    }
}
