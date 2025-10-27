package kakaotech.community.global.exception;

import kakaotech.community.global.exception.code.ExceptionCode;

public class CommentException extends BaseException{
    public CommentException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
