package com.yedu.bizppurio.support.application.dto.req.content;

public record EmphasizeButtonMessage(
    String message, String title, String senderkey, String templatecode, CommonButton[] button)
    implements Message {
  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String getTemplateCode() {
    return templatecode;
  }

  @Override
  public CommonButton[] getButtons() {
    return button;
  }
}
