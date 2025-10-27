package kakaotech.community.global.exception;

import kakaotech.community.global.exception.code.ExceptionCode;

public class AuthException extends BaseException {
    public AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
