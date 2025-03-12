package com.yedu.backend.domain.teacher.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TeacherCode {
    INACTIVE_TEACHER("EX101");

    private final String code;
}
