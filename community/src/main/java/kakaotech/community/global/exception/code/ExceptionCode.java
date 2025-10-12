package kakaotech.community.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // User
    USER_NOT_FOUND("찾을 수 없는 유저입니다.", NOT_FOUND),

    // Auth
    FAILED_TO_LOGIN("잘못된 이메일 혹은 비밀번호입니다.", UNAUTHORIZED),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    public int getStatus() {
        return httpStatus.value();
    }
}
