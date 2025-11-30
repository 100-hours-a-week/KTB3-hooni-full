package kakaotech.community.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // User
    USER_NOT_FOUND("찾을 수 없는 유저입니다.", NOT_FOUND),
    DUPLICATED_EMAIL_OR_NICKNAME("이미 존재하는 이메일 혹은 닉네임입니다.", CONFLICT),

    // Auth
    FAILED_TO_LOGIN("잘못된 이메일 혹은 비밀번호입니다.", UNAUTHORIZED),
    AUTH_TOKEN_NOT_FOUND("인증 토큰을 찾을 수 없습니다", UNAUTHORIZED),
    INVALID_AUTH_TOKEN("유효하지 않은 토큰입니다.", UNAUTHORIZED),
    AUTH_REQUIRED_REQUEST("인증이 필요한 요청입니다", UNAUTHORIZED),
    RE_LOGIN_REQUIRED("로그인 세션이 만료되었습니다. 재로그인해주세요", UNAUTHORIZED),

    // Post
    POST_NOT_FOUND("게시글을 찾을 수 없습니다", NOT_FOUND),
    POST_WRITER_MISMATCH("작성 권한이 없습니다", FORBIDDEN),

    // Image
    EMPTY_IMAGE("이미지가 비어있습니다.", BAD_REQUEST),
    IS_NOT_IMAGE_EXTENSION("이미지 타입이 아닙니다.", BAD_REQUEST),
    IMAGE_NOT_FOUND("이미지를 찾을 수 없습니다.", NOT_FOUND),
    FAILED_TO_UPLOAD_IMAGE("이미지 업로드 중 서버에서 문제 발생", INTERNAL_SERVER_ERROR),

    // Comment
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다", NOT_FOUND),
    COMMENT_WRITER_MISMATCH("작성 권한이 없습니다", FORBIDDEN),
    INVALID_COMMENT_CURSOR("커서 값이 올바르지 않습니다", BAD_REQUEST),

    ;

    private final String message;
    private final HttpStatus httpStatus;

    public int getStatus() {
        return httpStatus.value();
    }
}
