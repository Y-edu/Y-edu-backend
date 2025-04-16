package com.yedu.common.event.discord;

public record AlarmTalkErrorWithFirstEvent(
        String phoneNumber, String content, String code
) {
}
