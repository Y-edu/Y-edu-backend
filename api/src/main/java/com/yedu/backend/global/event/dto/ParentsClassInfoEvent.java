package com.yedu.backend.global.event.dto;

import java.util.List;

public record ParentsClassInfoEvent(
        String nickName,
        int classCount,
        List<String> days,
        List<String> startTime,
        List<Integer> classMinute,
        String teacherPhoneNumber,
        String parentsPhoneNumber
) {
    public record ClassTime (

    ){}
}
