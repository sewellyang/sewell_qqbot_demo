package com.sewell.qqbot.exception;

import lombok.Getter;

/**
 * API请求异常
 */
@Getter
public class ApiException extends RuntimeException {
    private final Integer code;
    private final String error;
    private final String traceId;

    public ApiException(Integer code, String message, String error, String traceId) {
        super(message);
        this.code = code;
        this.error = error;
        this.traceId = traceId;
    }
}
