package com.yedu.common.event.bizppurio;

import java.time.LocalDate;

public record TeacherWithScheduleCompleteTalkEvent(
    String applicationFormId,
    String teacherPhoneNumber,
    LocalDate sessionDate,
    String completeSessionToken,
    String changeSessionToken) {}
