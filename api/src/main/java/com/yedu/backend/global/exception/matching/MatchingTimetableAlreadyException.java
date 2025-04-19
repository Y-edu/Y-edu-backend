package com.yedu.backend.global.exception.matching;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.matching.constant.MatchingErrorCode.ALREADY_MATCHING_TIMETABLE;


public class MatchingTimetableAlreadyException extends ApplicationException {

    public MatchingTimetableAlreadyException(long classMatchingId) {
        super(ALREADY_MATCHING_TIMETABLE.getCode(), String.format(ALREADY_MATCHING_TIMETABLE.getMessage(), classMatchingId));
    }
}
