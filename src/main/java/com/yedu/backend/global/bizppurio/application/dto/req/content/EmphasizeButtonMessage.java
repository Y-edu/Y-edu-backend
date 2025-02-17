package com.yedu.backend.global.bizppurio.application.dto.req.content;

public record EmphasizeButtonMessage(
        String message,
        String title,
        String header,
        String senderkey,
        String templatecode,
        CommonButton[] button
) implements Message{
    @Override
    public String getMessage() {
        return message;
    }
}
