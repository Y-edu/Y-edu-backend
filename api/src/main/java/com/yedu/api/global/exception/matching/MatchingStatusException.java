package com.yedu.api.global.exception.matching;

import static com.yedu.api.global.exception.matching.constant.MatchingErrorCode.NOT_WAITING;

import com.yedu.api.global.exception.ApplicationException;

public class MatchingStatusException extends ApplicationException {
  public MatchingStatusException(long classMatchingId) {
    super(NOT_WAITING.getCode(), String.format(NOT_WAITING.getMessage(), classMatchingId));
  }
}
