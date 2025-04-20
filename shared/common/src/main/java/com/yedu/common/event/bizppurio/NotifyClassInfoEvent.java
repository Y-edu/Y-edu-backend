package com.yedu.common.event.bizppurio;

public record NotifyClassInfoEvent(
    String online,
    String applicationFormId,
    String nickName,
    String classType,
    String district,
    String dong,
    long teacherId,
    String phoneNumber) {}
