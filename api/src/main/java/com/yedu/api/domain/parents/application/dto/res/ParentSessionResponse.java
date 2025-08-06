package com.yedu.api.domain.parents.application.dto.res;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.ClassSessions;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record ParentSessionResponse(
    String applicationFormId,
    String teacherNickname,
    List<Session> sessions
    ) {

  @Builder
  public record Session(
      Long sessionId,
      LocalDate sessionDate,
      Integer roundNumber
  ){

  }

  public static List<ParentSessionResponse> of(Map<ClassMatching, List<ClassSession>> sessionWithMatching){

    return sessionWithMatching.entrySet().stream().map(entry->{
      ClassMatching matching = entry.getKey();
      Integer maxRoundNumber = matching.getApplicationForm().maxRoundNumber();
      List<ClassSession> sessions = new ClassSessions(entry.getValue()).calculateRoundWithReset(
          maxRoundNumber);

      List<Session> sessionResponse = sessions.stream().map(it -> Session.builder()
          .sessionId(it.getClassSessionId())
          .sessionDate(it.getSessionDate())
          .roundNumber(it.getRound())
          .build()).toList();

      return ParentSessionResponse.builder()
          .applicationFormId(matching.getApplicationForm().getApplicationFormId())
          .teacherNickname(matching.getTeacher().getTeacherInfo().getNickName())
          .sessions(sessionResponse)
          .build();
    }).toList();
  }

}
