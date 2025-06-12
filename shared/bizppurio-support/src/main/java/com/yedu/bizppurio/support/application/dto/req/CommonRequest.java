package com.yedu.bizppurio.support.application.dto.req;

public record CommonRequest<T extends CommonContent>(
    String account, String type, String from, String to, T content, String refkey) {}
