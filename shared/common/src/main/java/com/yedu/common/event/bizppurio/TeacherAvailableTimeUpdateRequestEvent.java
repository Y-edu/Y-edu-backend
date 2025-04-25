package com.yedu.common.event.bizppurio;

public record TeacherAvailableTimeUpdateRequestEvent(
    String name, String token, String teacherPhoneNumber) {}
