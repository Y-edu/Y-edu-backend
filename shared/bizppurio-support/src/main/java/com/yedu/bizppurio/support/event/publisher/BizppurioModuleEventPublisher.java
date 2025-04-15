package com.yedu.bizppurio.support.event.publisher;

import com.yedu.common.event.discord.AlarmTalkErrorMessageEvent;
import com.yedu.common.event.discord.AlarmTalkErrorWithFirstEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BizppurioModuleEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishAlarmTalkErrorWithFirstEvent(AlarmTalkErrorWithFirstEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishAlarmTalkErrorMessageEvent(AlarmTalkErrorMessageEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
