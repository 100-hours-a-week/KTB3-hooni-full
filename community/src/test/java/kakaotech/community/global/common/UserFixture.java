package kakaotech.community.global.common;

import kakaotech.community.domain.user.User;

import java.util.UUID;

public class UserFixture {

    private UserFixture() {
    }

    static User one(String email, String password, String nickname, UUID imageId) {
        return new User(
                email == null ? "email1@email.com" : email,
                password == null ? "@ASDasd123" : password,
                nickname == null ? "test1" : nickname,
                imageId
                );
    }

    static User one(UUID imageId) {
        return one(null, null, null, imageId);
    }
}
