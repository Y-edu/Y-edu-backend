package com.yedu.api.global.exception.teacher;

import static com.yedu.api.global.exception.teacher.constant.TeacherErrorCode.LOGIN_FAIL;

import com.yedu.api.global.exception.ApplicationException;

public class TeacherLoginFailException extends ApplicationException {
  public TeacherLoginFailException(String name, String phoneNumber) {
    super(LOGIN_FAIL.getCode(), String.format(LOGIN_FAIL.getMessage(), name, phoneNumber));
  }
}
