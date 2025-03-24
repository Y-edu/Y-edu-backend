package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_PHONE;

public class TeacherNotFoundByPhoneNumberException extends ApplicationException {
    public TeacherNotFoundByPhoneNumberException(String phoneNumber) {
        super(NOTFOUND_BY_PHONE.getCode(), String.format(
                NOTFOUND_BY_PHONE.getMessage(),
                phoneNumber
        ));
    }
}
