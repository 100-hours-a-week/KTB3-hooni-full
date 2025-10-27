package kakaotech.community.global.exception;

import kakaotech.community.global.exception.code.ExceptionCode;

public class PostException extends BaseException{
    public PostException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
