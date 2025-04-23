package com.yedu.common.event.bizppurio;

public record PayNotificationEvent(String parentPhoneNumber, String nickName, int pay) {}
