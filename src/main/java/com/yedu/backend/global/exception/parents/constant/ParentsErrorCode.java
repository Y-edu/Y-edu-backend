package com.yedu.backend.global.exception.parents.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ParentsErrorCode {
    NOTFOUND_INFO("EX301"),
    ;

    private final String code;
}
