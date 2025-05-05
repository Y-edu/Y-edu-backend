package com.yedu.api.global.exception.teacher;

import static com.yedu.api.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_ID;

import com.yedu.api.global.exception.ApplicationException;

public class TeacherNotFoundByIdException extends ApplicationException {
  public TeacherNotFoundByIdException(long teacherId) {
    super(NOTFOUND_BY_ID.getCode(), String.format(NOTFOUND_BY_ID.getMessage(), teacherId));
  }
}
