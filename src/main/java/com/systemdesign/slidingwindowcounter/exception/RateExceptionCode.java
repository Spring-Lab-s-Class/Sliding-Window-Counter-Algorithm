package com.systemdesign.slidingwindowcounter.exception;

import com.systemdesign.slidingwindowcounter.common.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Getter
@RequiredArgsConstructor
public enum RateExceptionCode implements ExceptionCode {

    COMMON_TOO_MANY_REQUESTS(TOO_MANY_REQUESTS, "RAT-001", "사용자 요청 횟수 초과"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
