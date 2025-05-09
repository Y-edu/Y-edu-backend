package com.yedu.consumer.domain.notification.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiverType {
  PARENT("학부모"),
  TEACHER("선생님");

  private final String desc;
}
