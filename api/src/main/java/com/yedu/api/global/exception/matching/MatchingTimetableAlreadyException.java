package com.yedu.api.global.exception.matching;

import static com.yedu.api.global.exception.matching.constant.MatchingErrorCode.ALREADY_MATCHING_TIMETABLE;

import com.yedu.api.global.exception.ApplicationException;

public class MatchingTimetableAlreadyException extends ApplicationException {

  public MatchingTimetableAlreadyException(long classMatchingId) {
    super(
        ALREADY_MATCHING_TIMETABLE.getCode(),
        String.format(ALREADY_MATCHING_TIMETABLE.getMessage(), classMatchingId));
  }
}
