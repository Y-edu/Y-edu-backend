package com.yedu.backend.bizppurio.application.dto.req.content;

public record ButtonMessage(
        String message,
        String senderkey,
        String templatecode,
        CommonButton[] button
) implements Message {
    @Override
    public String getMessage() {
        return message;
    }
}
