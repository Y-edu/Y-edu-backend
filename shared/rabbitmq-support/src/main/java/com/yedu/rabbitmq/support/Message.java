package com.yedu.rabbitmq.support;

public record Message(Class<?> type, Object data) {}
