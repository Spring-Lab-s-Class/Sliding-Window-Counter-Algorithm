package com.systemdesign.slidingwindowcounter.common.exception;

public class CommonException extends BusinessException {

    public CommonException(ExceptionCode exceptionCode, Object... rejectedValues) {
        super(exceptionCode, rejectedValues);
    }
}
