package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode;
import com.yedu.backend.global.exception.ApplicationException;

public class InActiveTeacherException extends ApplicationException {
    public InActiveTeacherException() {
        super(TeacherErrorCode.INACTIVE_TEACHER.getCode());
    }
}
