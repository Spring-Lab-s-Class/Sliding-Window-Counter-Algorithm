package com.systemdesign.slidingwindowcounter.common.exception;

public class RateLimitExceededException extends BusinessException {

    public RateLimitExceededException(ExceptionCode exceptionCode, Object... rejectedValues) {
        super(exceptionCode, rejectedValues);
    }
}
