package com.yedu.common.event.bizppurio;


public record TeacherWithScheduleCompleteTalkWeeklyRemindEvent(
    String teacherPhoneNumber,
    String token) {}
