package kakaotech.community.global.exception;

import kakaotech.community.global.exception.code.ExceptionCode;

public class ImageException extends BaseException{
    public ImageException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
