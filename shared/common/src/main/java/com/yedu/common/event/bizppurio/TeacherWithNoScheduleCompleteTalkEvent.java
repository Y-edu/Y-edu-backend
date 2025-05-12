package com.yedu.common.event.bizppurio;

public record TeacherWithNoScheduleCompleteTalkEvent(
    String applicationFormId, String teacherPhoneNumber, String token) {}
