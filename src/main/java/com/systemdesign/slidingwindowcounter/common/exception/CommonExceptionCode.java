package com.systemdesign.slidingwindowcounter.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {

    COMMON_NOT_FOUND(NOT_FOUND, "COM-001", "요청한 URL에 해당하는 리소스를 찾을 수 없음"),
    COMMON_BAD_REQUEST(BAD_REQUEST, "COM-002", "잘못된 요청"),
    COMMON_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "COM-003", "허용되지 않은 HTTP Method 요청 발생"),
    COMMON_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "COM-004", "기타 서버 내부 에러 발생"),
    COMMON_TOO_MANY_REQUESTS(TOO_MANY_REQUESTS,"COM-005", "사용자 요청 횟수 초과"),
            ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
