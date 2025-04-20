package com.yedu.backend.global.exception.teacher;

import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_BY_ID;

import com.yedu.backend.global.exception.ApplicationException;

public class TeacherNotFoundByIdException extends ApplicationException {
  public TeacherNotFoundByIdException(long teacherId) {
    super(NOTFOUND_BY_ID.getCode(), String.format(NOTFOUND_BY_ID.getMessage(), teacherId));
  }
}
