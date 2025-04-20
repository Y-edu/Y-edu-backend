package com.yedu.backend.global.exception.matching;

import static com.yedu.backend.global.exception.matching.constant.MatchingErrorCode.NOTFOUND_INFO;
import static com.yedu.backend.global.exception.matching.constant.MatchingErrorCode.NOTFOUND_INFO_WITH_MATCHING_ID;

import com.yedu.backend.global.exception.ApplicationException;

public class MatchingNotFoundException extends ApplicationException {

  public MatchingNotFoundException(Long matchingId) {
    super(
        NOTFOUND_INFO_WITH_MATCHING_ID.getCode(),
        NOTFOUND_INFO_WITH_MATCHING_ID.formatMessage(matchingId.toString()));
  }

  public MatchingNotFoundException(String applicationFormId, long teacherId, String phoneNumber) {
    super(
        NOTFOUND_INFO.getCode(),
        NOTFOUND_INFO.formatMessage(applicationFormId, String.valueOf(teacherId), phoneNumber));
  }
}
