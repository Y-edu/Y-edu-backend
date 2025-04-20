package com.yedu.common.event.discord;

public record TeacherRegisterEvent(
    String teacherName,
    String teacherNickName,
    String subject,
    String teacherLink,
    String region) {}
