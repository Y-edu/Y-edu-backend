package com.yedu.bizppurio.support.application.dto.req.content;

public record SimpleButton(String name, String type) implements CommonButton {

  @Override
  public String getButtonName() {
    return name;
  }

  @Override
  public String getButtonLink() {
    return null;
  }
}
