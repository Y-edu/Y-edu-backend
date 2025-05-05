package com.yedu.common.event.bizppurio;

import com.yedu.common.dto.MessageStatusRequest;

public record BizppurioWebHookEvent(MessageStatusRequest request) {}
