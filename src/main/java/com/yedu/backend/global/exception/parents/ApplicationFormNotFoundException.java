package com.yedu.backend.global.exception.parents;

import com.yedu.backend.global.exception.ApplicationException;
import static com.yedu.backend.global.exception.parents.constant.ParentsErrorCode.NOTFOUND_INFO;

public class ApplicationFormNotFoundException extends ApplicationException {
    public ApplicationFormNotFoundException(String applicationFormId) {
        super(NOTFOUND_INFO.getCode(), String.format(
                        NOTFOUND_INFO.getMessage(),
                        applicationFormId
                )
        );
    }
}
