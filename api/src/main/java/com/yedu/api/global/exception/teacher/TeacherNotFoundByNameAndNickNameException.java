package com.yedu.api.global.exception.teacher;

import static com.yedu.api.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_NAME_NICKNAME;

import com.yedu.api.global.exception.ApplicationException;

public class TeacherNotFoundByNameAndNickNameException extends ApplicationException {
  public TeacherNotFoundByNameAndNickNameException(String name, String nickName) {
    super(
        NOTFOUND_BY_NAME_NICKNAME.getCode(),
        String.format(NOTFOUND_BY_NAME_NICKNAME.getMessage(), name, nickName));
  }
}
