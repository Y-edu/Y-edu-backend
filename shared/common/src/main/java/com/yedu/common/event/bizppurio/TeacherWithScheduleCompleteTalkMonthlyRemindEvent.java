package com.yedu.common.event.bizppurio;

public record TeacherWithScheduleCompleteTalkMonthlyRemindEvent(
    String teacherPhoneNumber,
    String token) {}
