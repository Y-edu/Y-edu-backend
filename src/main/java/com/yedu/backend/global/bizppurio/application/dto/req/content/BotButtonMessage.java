package com.yedu.backend.global.bizppurio.application.dto.req.content;

public record BotButtonMessage(
        String message,
        String senderkey,
        String templatecode,
        BotButton[] button
) implements Message {
}
