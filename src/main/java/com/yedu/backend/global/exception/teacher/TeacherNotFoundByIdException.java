package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_ID;

public class TeacherNotFoundByIdException extends ApplicationException {
    public TeacherNotFoundByIdException() {
        super(NOTFOUND_BY_ID.getCode());
    }
}
