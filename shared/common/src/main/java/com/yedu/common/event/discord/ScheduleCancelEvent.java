package com.yedu.common.event.discord;


public record ScheduleCancelEvent(
        String parentsName,
        String teacherName,
        String refuseReason
) {
}
