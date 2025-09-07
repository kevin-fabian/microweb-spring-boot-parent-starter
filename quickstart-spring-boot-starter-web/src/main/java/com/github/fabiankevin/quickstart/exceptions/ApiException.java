package com.github.fabiankevin.quickstart.exceptions;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private int httpStatusCode;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message, int httpStatusCode, Throwable cause) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
    }

    public ApiException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

}
