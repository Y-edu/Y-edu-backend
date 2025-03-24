package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.LOGIN_FAIL;

public class TeacherLoginFailException extends ApplicationException {
    public TeacherLoginFailException() {
        super(LOGIN_FAIL.getCode());
    }
}
