package com.yedu.common.event.discord;

public record AlarmTalkErrorInfoEvent(
    String phoneNumber, String content, String code, String message) {}
