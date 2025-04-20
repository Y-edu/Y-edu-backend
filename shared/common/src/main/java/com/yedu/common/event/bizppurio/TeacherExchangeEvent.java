package com.yedu.common.event.bizppurio;

public record TeacherExchangeEvent(
    String applicationFormId,
    String classCount,
    String time,
    String age,
    String district,
    int money,
    String parentsPhoneNumber,
    String teacherPhoneNumber,
    String managementId) {}
