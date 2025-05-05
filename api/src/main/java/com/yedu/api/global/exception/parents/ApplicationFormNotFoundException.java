package com.yedu.api.global.exception.parents;

import static com.yedu.api.global.exception.parents.constant.ParentsErrorCode.NOTFOUND_INFO;

import com.yedu.api.global.exception.ApplicationException;

public class ApplicationFormNotFoundException extends ApplicationException {
  public ApplicationFormNotFoundException(String applicationFormId) {
    super(NOTFOUND_INFO.getCode(), String.format(NOTFOUND_INFO.getMessage(), applicationFormId));
  }
}
