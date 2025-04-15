package com.yedu.bizppurio.support.event.mapper;

import com.yedu.common.event.discord.AlarmTalkErrorInfoEvent;
import com.yedu.common.event.discord.AlarmTalkErrorMessageEvent;
import com.yedu.common.event.discord.AlarmTalkErrorWithFirstEvent;

public class DiscordEventMapper {
    public static AlarmTalkErrorWithFirstEvent mapToAlarmTalkErrorWithFirstEvent(String phoneNumber, String content, String code) {
        return new AlarmTalkErrorWithFirstEvent(phoneNumber, content, code);
    }

    public static AlarmTalkErrorInfoEvent mapToAlarmTalkErrorInfoEvent(String phoneNumber, String content, String code, String message) {
        return new AlarmTalkErrorInfoEvent(phoneNumber, content, code, message);
    }

    public static AlarmTalkErrorMessageEvent mapToAlarmTalkErrorMessageEvent(String message) {
        return new AlarmTalkErrorMessageEvent(message);
    }
}
