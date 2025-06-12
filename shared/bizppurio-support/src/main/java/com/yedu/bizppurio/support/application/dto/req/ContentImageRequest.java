package com.yedu.bizppurio.support.application.dto.req;

import com.yedu.bizppurio.support.application.dto.req.content.Message;

public record ContentImageRequest(Message ai) implements CommonContent {

  @Override
  public Message getContent() {
    return ai;
  }
}
