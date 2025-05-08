package com.yedu.common.event.bizppurio;

public record TeacherScheduleEvent(
    String parentsPhoneNumber, String teacherPhoneNumber, String classManagementToken) {}
