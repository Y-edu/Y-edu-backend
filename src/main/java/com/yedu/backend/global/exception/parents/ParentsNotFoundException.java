package com.yedu.backend.global.exception.parents;

import com.yedu.backend.global.exception.ApplicationException;
import static com.yedu.backend.global.exception.parents.constant.ParentsErrorCode.NOTFOUND_INFO;

public class ParentsNotFoundException extends ApplicationException {
    public ParentsNotFoundException() {
        super(NOTFOUND_INFO.getCode());
    }
}
