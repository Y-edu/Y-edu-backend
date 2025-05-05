package com.yedu.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioCheckStep;
import com.yedu.common.event.bizppurio.BizppurioWebHookEvent;
import com.yedu.rabbitmq.support.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SystemMessageConsumer extends AbstractConsumer {

  private final BizppurioCheckStep bizppurioCheckStep;

  public SystemMessageConsumer(ObjectMapper objectMapper, BizppurioCheckStep bizppurioCheckStep) {
    super(objectMapper);
    this.bizppurioCheckStep = bizppurioCheckStep;
  }

  @PostConstruct
  void init() {
    handlers.put(BizppurioWebHookEvent.class, msg -> {
      BizppurioWebHookEvent event = convert(msg, BizppurioWebHookEvent.class);
      bizppurioCheckStep.checkByWebHook(event.request());
    });
  }

  @Override
  void afterConsume(Message message) {

  }
}
