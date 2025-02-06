package com.yedu.backend.global.bizppurio.application.dto.req.content;

public record ButtonMessage(
        String message,
        String senderkey,
        String templatecode,
        CommonButton[] button
) implements Message {
}
