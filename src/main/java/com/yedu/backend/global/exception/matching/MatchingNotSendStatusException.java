package com.yedu.backend.global.exception.matching;

import static com.yedu.backend.global.exception.matching.constant.MatchingErrorCode.NOT_SEND;

import com.yedu.backend.global.exception.ApplicationException;

public class MatchingNotSendStatusException extends ApplicationException {

  public MatchingNotSendStatusException(long classMatchingId) {
    super(NOT_SEND.getCode(), String.format(NOT_SEND.getMessage(), classMatchingId)
    );
  }

}
