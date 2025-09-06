package com.yedu.common.event.bizppurio;

import java.time.LocalDate;

public record TeacherClassPauseEvent(
    String teacherPhoneNumber,
    String applicationFormId,
    LocalDate sessionDate
) {

}
