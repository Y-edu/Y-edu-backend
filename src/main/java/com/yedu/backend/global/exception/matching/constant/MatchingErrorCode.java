package com.yedu.backend.global.exception.matching.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MatchingErrorCode {
    NOTFOUND_INFO("EX201"),
    NOT_WAITING("EX202"),
    ;

    private final String code;
}
