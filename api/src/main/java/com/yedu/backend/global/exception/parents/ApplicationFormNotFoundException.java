package com.yedu.backend.global.exception.parents;

import static com.yedu.backend.global.exception.parents.constant.ParentsErrorCode.NOTFOUND_INFO;

import com.yedu.backend.global.exception.ApplicationException;

public class ApplicationFormNotFoundException extends ApplicationException {
  public ApplicationFormNotFoundException(String applicationFormId) {
    super(NOTFOUND_INFO.getCode(), String.format(NOTFOUND_INFO.getMessage(), applicationFormId));
  }
}
