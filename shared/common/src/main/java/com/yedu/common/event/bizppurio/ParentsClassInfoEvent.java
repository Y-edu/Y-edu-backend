package com.yedu.common.event.bizppurio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ParentsClassInfoEvent(
        String nickName,
        List<ClassTime> classTimes,
        FirstDay firstDay,
        String book,
        String parentsPhoneNumber
) {
    public record ClassTime (
            String day,
            LocalTime startTime,
            int classMinute
    ){}

    public record FirstDay(
            LocalDate date,
            LocalTime start
    ){}
}
