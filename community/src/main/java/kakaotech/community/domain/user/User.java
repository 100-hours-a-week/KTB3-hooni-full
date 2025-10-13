package kakaotech.community.domain.user;

import kakaotech.community.global.entity.BaseEntity;
import kakaotech.community.global.exception.AuthException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.FAILED_TO_LOGIN;

@Getter
public class User extends BaseEntity {
    private Long id;
    private String email;
    private String password; // TODO. Encoding 필요
    private String nickname;
    private UUID profileImageId;

    public User(
            String email,
            String password,
            String nickname,
            UUID profileImageId
    ) {
        super(LocalDateTime.now());
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageId = profileImageId;
    }

    boolean isSameEmail(String email) {
        return this.email.equals(email);
    }

    boolean isSameNickname(String nickname) {
        return this.nickname.equals(nickname);
    }

    public void validateLoginable(String password) {
        if (!this.password.equals(password)) {
            throw new AuthException(FAILED_TO_LOGIN);
        }
    }

    void assignId(long id) {
        this.id = id;
    }
}
