package com.yedu.backend.domain.teacher.exception;

import com.yedu.backend.domain.teacher.presentation.constant.TeacherCode;
import com.yedu.backend.global.exception.ApplicationException;

public class InActiveTeacherException extends ApplicationException {
    public InActiveTeacherException() {
        super(TeacherCode.INACTIVE_TEACHER.getCode());
    }
}
