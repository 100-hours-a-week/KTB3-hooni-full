package kakaotech.community.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kakaotech.community.domain.user.port.encode.Encoder;
import kakaotech.community.global.entity.BaseEntity;
import kakaotech.community.global.exception.AuthException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static kakaotech.community.global.exception.code.ExceptionCode.FAILED_TO_LOGIN;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private UUID profileImage;

    public User(
            String email,
            String password,
            String nickname,
            UUID profileImageId
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImageId;
    }

    boolean isSameEmail(String email) {
        return this.email.equals(email);
    }

    boolean isSameNickname(String nickname) {
        return this.nickname.equals(nickname);
    }

    public void validateLoginable(Encoder encoder, String password) {
        if (!encoder.matches(password, this.password)) {
            throw new AuthException(FAILED_TO_LOGIN);
        }
    }

    void assignId(long id) {
        this.id = id;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(UUID uuid) {
        this.profileImage = uuid;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
