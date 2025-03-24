package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.INACTIVE_TEACHER;

public class InActiveTeacherException extends ApplicationException {
    public InActiveTeacherException(long teacherId) {
        super(INACTIVE_TEACHER.getCode(), String.format(
                        INACTIVE_TEACHER.getMessage(),
                        teacherId
                )
        );
    }
}
