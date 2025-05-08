package com.yedu.bizppurio.support.application.dto.req.content;

public record TextMessage(String message, String senderkey, String templatecode)
    implements Message {
  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String getTemplateCode() {
    return templatecode;
  }
}
