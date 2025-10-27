package kakaotech.community.domain.user.service;

import kakaotech.community.domain.user.User;
import kakaotech.community.domain.user.dto.UserResponse;

import java.util.UUID;

public class UserMapper {

    static User toEntity(String email, String password, String nickname, UUID imageId) {
        return new User(email, password, nickname, imageId);
    }

    static UserResponse.Update toUpdated(User user) {
        return new UserResponse.Update(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageId()
        );
    }
}
