package com.yedu.backend.global.bizppurio.application.dto.req.content;

public record TextMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {
}
