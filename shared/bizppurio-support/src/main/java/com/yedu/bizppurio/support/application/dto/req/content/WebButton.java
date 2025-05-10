package com.yedu.bizppurio.support.application.dto.req.content;

public record WebButton(String name, String type, String url_mobile, String url_pc)
    implements CommonButton {

  @Override
  public String getButtonName() {
    return name;
  }

  @Override
  public String getButtonLink() {
    return url_pc;
  }
}
