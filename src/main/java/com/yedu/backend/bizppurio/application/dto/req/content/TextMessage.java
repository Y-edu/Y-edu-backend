package com.yedu.backend.bizppurio.application.dto.req.content;

public record TextMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {
    @Override
    public String getMessage() {
        return message;
    }
}
