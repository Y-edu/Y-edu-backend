package com.yedu.backend.global.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{
    private final String code;

    protected ApplicationException(String code) {
        this.code = code;
    }
}