package kakaotech.community.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import kakaotech.community.domain.common.validator.Email;
import kakaotech.community.domain.common.validator.Nickname;
import kakaotech.community.domain.common.validator.Password;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    public record Login(
            @Email String email,
            @Password String password
    ) {
    }

    public record Join(
            @Email String email,
            @Password String password,
            @Nickname String nickname,
            @NotNull MultipartFile image
    ) {
    }
}
