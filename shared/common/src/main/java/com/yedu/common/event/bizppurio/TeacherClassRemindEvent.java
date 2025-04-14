package com.yedu.common.event.bizppurio;

public record TeacherClassRemindEvent(
        String nickName,
        String phoneNumber,
        long managementId
) {
}
