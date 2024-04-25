package com.systemdesign.slidingwindowcounter.exception;

import com.systemdesign.slidingwindowcounter.common.exception.BusinessException;
import com.systemdesign.slidingwindowcounter.common.exception.ExceptionCode;

public class RateLimitExceededException extends BusinessException {

    public RateLimitExceededException(ExceptionCode exceptionCode, Object... rejectedValues) {
        super(exceptionCode, rejectedValues);
    }
}
