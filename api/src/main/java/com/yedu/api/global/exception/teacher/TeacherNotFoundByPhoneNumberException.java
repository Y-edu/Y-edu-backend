package com.yedu.api.global.exception.teacher;

import static com.yedu.api.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_PHONE;

import com.yedu.api.global.exception.ApplicationException;

public class TeacherNotFoundByPhoneNumberException extends ApplicationException {
  public TeacherNotFoundByPhoneNumberException(String phoneNumber) {
    super(NOTFOUND_BY_PHONE.getCode(), String.format(NOTFOUND_BY_PHONE.getMessage(), phoneNumber));
  }
}
