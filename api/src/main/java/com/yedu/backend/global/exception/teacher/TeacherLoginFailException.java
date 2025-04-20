package com.yedu.backend.global.exception.teacher;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.LOGIN_FAIL;

import com.yedu.backend.global.exception.ApplicationException;

public class TeacherLoginFailException extends ApplicationException {
  public TeacherLoginFailException(String name, String phoneNumber) {
    super(LOGIN_FAIL.getCode(), String.format(LOGIN_FAIL.getMessage(), name, phoneNumber));
  }
}
