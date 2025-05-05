package com.yedu.api.admin.application.dto.res;

import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.common.type.ClassType;
import java.util.List;

public record AllAlarmTalkResponse(
    int accept, int total, int time, List<AlarmTalkResponse> alarmTalkResponses) {
  public record AlarmTalkResponse(
      long classMatchingId,
      long teacherId,
      ClassType subject,
      MatchingStatus status,
      String nickName,
      String name,
      int responseTime,
      int accept,
      int total,
      String refuseReason,
      String phoneNumber) {}
}
