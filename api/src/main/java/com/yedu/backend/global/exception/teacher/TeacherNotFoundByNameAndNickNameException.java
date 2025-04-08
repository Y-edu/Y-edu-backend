package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_NAME_NICKNAME;

public class TeacherNotFoundByNameAndNickNameException extends ApplicationException {
    public TeacherNotFoundByNameAndNickNameException(String name, String nickName) {
        super(NOTFOUND_BY_NAME_NICKNAME.getCode(), String.format(
                NOTFOUND_BY_NAME_NICKNAME.getMessage(),
                name,
                nickName
        ));
    }
}
