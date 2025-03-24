package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_NICKNAME_NAME;

public class TeacherNotFoundByNameAndNickNameException extends ApplicationException {
    public TeacherNotFoundByNameAndNickNameException() {
        super(NOTFOUND_BY_NICKNAME_NAME.getCode());
    }
}
