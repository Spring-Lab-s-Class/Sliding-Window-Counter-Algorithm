package com.systemdesign.slidingwindowcounter.common.exception;

import com.systemdesign.slidingwindowcounter.common.dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.systemdesign.slidingwindowcounter.common.exception.CommonExceptionCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ResponseEntity.status(COMMON_NOT_FOUND.getStatus())
                .body(ExceptionResponse.from(COMMON_NOT_FOUND.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(COMMON_BAD_REQUEST.getStatus())
                .body(ExceptionResponse.from(COMMON_BAD_REQUEST.getCode()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(COMMON_METHOD_NOT_ALLOWED.getStatus())
                .body(ExceptionResponse.from(COMMON_METHOD_NOT_ALLOWED.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        return ResponseEntity.status(COMMON_INTERNAL_SERVER_ERROR.getStatus())
                .body(ExceptionResponse.from(COMMON_INTERNAL_SERVER_ERROR.getCode()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        return ResponseEntity.status(exceptionCode.getStatus())
                .body(ExceptionResponse.from(exceptionCode.getCode()));
    }
}
