package com.yedu.api.admin.presentation;

import com.yedu.common.dto.MessageStatusRequest;
import com.yedu.common.event.bizppurio.BizppurioWebHookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {

  private final ApplicationEventPublisher eventPublisher;

  @PostMapping("/bizppurio/result/webhook")
  public void resultWebHook(@RequestBody MessageStatusRequest request) {
    eventPublisher.publishEvent(new BizppurioWebHookEvent(request));
  }
}
