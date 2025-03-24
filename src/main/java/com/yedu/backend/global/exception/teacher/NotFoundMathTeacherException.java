package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_MATH;

public class NotFoundMathTeacherException extends ApplicationException {
    public NotFoundMathTeacherException() {
        super(NOTFOUND_MATH.getCode());
    }
}
