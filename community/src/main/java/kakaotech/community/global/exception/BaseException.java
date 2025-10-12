package kakaotech.community.global.exception;

import kakaotech.community.global.exception.code.ExceptionCode;

public class BaseException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public BaseException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public int getHttpStatus() {
        return exceptionCode.getStatus();
    }
}
