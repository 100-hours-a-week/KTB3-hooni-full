package kakaotech.community.global.exception;

import kakaotech.community.global.exception.code.ExceptionCode;

public class UserException extends BaseException {
    public UserException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
