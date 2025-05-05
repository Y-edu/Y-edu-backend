package com.yedu.api.global.exception.teacher;

import static com.yedu.api.global.exception.teacher.constant.TeacherErrorCode.INACTIVE_TEACHER;

import com.yedu.api.global.exception.ApplicationException;

public class InActiveTeacherException extends ApplicationException {
  public InActiveTeacherException(long teacherId) {
    super(INACTIVE_TEACHER.getCode(), String.format(INACTIVE_TEACHER.getMessage(), teacherId));
  }
}
