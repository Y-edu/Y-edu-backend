package com.yedu.backend.global.exception.parents.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ParentsErrorCode {
    NOTFOUND_INFO("EX301", "해당하는 신청서를 찾을 수 없습니다 - applicationFormId : %s"),
    ;

    private final String code;
    private final String message;
}
