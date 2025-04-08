package com.yedu.backend.global.event.dto;

public record ParentsClassInfoEvent(
        String nickName,
        int classCount,
        int teachingTime,
        String teacherPhoneNumber,
        String parentsPhoneNumber
) {
}
