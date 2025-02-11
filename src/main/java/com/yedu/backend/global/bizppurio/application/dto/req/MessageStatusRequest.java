package com.yedu.backend.global.bizppurio.application.dto.req;

public record MessageStatusRequest(
    String DEVICE,
    String CMSGID,
    String MSGID,
    String PHONE,
    String MEDIA,
    String UNIXTIME,
    String RESULT,
    String RETRY_FLAG,
    String REFKEY
) {
}
