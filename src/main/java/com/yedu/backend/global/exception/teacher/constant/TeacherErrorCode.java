package com.yedu.backend.global.exception.teacher.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TeacherErrorCode {
    INACTIVE_TEACHER("EX101"),
    NOTFOUND_BY_ID("EX102"),
    NOTFOUND_BY_NICKNAME_NAME("EX103"),
    NOTFOUND_BY_PHONE("EX104"),
    NOTFOUND_ENGLISH("EX105"),
    NOTFOUND_MATH("EX106"),
    LOGIN_FAIL("EX107")
    ;

    private final String code;
}
