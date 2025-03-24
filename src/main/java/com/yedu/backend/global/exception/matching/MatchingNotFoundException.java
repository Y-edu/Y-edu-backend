package com.yedu.backend.global.exception.matching;

import com.yedu.backend.global.exception.ApplicationException;
import static com.yedu.backend.global.exception.matching.constant.MatchingErrorCode.NOTFOUND_INFO;

public class MatchingNotFoundException extends ApplicationException {
    public MatchingNotFoundException() {
        super(NOTFOUND_INFO.getCode());
    }
}
