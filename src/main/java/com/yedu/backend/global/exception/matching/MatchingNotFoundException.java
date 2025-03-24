package com.yedu.backend.global.exception.matching;

import com.yedu.backend.global.exception.ApplicationException;
import static com.yedu.backend.global.exception.matching.constant.MatchingErrorCode.NOTFOUND_INFO;

public class MatchingNotFoundException extends ApplicationException {
    public MatchingNotFoundException(String applicationFormId, long teacherId, String phoneNumber) {
        super(NOTFOUND_INFO.getCode(), String.format(
                        NOTFOUND_INFO.getMessage(),
                        applicationFormId,
                        teacherId,
                        phoneNumber
                )
        );
    }
}
