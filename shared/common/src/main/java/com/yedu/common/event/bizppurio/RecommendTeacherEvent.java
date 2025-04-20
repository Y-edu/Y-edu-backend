package com.yedu.common.event.bizppurio;

public record RecommendTeacherEvent(
    String parentsPhoneNumber,
    String teacherNickName,
    String district,
    String classType,
    long teacherId,
    String token) {}
