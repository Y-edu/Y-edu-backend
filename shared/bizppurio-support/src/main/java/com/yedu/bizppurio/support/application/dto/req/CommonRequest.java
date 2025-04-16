package com.yedu.bizppurio.support.application.dto.req;

public record CommonRequest(
        String account,
        String type,
        String from,
        String to,
        ContentRequest content,
        String refkey
) {}
