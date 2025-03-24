package com.yedu.backend.global.exception.teacher;

import com.yedu.backend.global.exception.ApplicationException;
import static com.yedu.backend.global.exception.teacher.constant.TeacherErrorCode.NOTFOUND_ENGLISH;

public class NotFoundEnglishTeacherException extends ApplicationException {
    public NotFoundEnglishTeacherException(long teacherId) {
        super(NOTFOUND_ENGLISH.getCode(), String.format(
                        NOTFOUND_ENGLISH.getMessage(),
                        teacherId
                )
        );
    }
}
