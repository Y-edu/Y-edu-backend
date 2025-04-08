package com.yedu.backend.global.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{
    private final String code;
    private final String message;

    protected ApplicationException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
