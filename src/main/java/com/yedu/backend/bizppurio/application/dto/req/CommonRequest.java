package com.yedu.backend.bizppurio.application.dto.req;

public record CommonRequest(
        String account,
        String type,
        String from,
        String to,
        ContentRequest content,
        String refkey
) {}
