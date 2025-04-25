package com.yedu.common.event.bizppurio;

import java.time.LocalTime;
import java.util.List;

public record TeacherExchangeEvent(
    String applicationFormId,
    String classCount,
    String time,
    List<DayTime> dayTimes,
    String age,
    String district,
    int money,
    String parentsPhoneNumber,
    String teacherPhoneNumber,
    String classNotifyToken,
    String classManagementToken) {

  public record DayTime(String day, List<LocalTime> times) {}
}
