package kakaotech.community.global.exception.advice;

import kakaotech.community.global.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static kakaotech.community.global.exception.code.ExceptionCode.INVALID_ARGUMENT;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(BindException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", INVALID_ARGUMENT.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", INVALID_ARGUMENT.getMessage()));
    }
}
