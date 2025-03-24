package com.yedu.backend.global.exception.matching;

import com.yedu.backend.global.exception.ApplicationException;

import static com.yedu.backend.global.exception.matching.constant.MatchingErrorCode.NOT_WAITING;

public class MatchingStatusException extends ApplicationException {
    public MatchingStatusException(long classMatchingId) {
        super(NOT_WAITING.getCode(), String.format(
                        NOT_WAITING.getMessage(),
                        classMatchingId
                )
        );
    }
}
