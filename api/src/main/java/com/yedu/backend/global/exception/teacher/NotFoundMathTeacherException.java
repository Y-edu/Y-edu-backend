package com.yedu.backend.global.exception.teacher;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_MATH;

import com.yedu.backend.global.exception.ApplicationException;

public class NotFoundMathTeacherException extends ApplicationException {
  public NotFoundMathTeacherException(long teacherId) {
    super(NOTFOUND_MATH.getCode(), String.format(NOTFOUND_MATH.getMessage(), teacherId));
  }
}
