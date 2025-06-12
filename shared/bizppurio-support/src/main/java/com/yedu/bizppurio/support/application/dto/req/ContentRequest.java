package com.yedu.bizppurio.support.application.dto.req;

import com.yedu.bizppurio.support.application.dto.req.content.Message;

public record ContentRequest(Message at) implements CommonContent {

  @Override
  public Message getContent() {
    return at;
  }
}
