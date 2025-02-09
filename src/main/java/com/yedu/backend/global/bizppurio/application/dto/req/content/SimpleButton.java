package com.yedu.backend.global.bizppurio.application.dto.req.content;

public record SimpleButton(
        String name,
        String type
) implements CommonButton {
}
