package com.yedu.api.domain.parents.application.dto.res;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.ClassSessions;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import org.apache.commons.lang3.tuple.Pair;

@Builder
public record ParentSessionResponse(
    String applicationFormId, String teacherNickname, List<Session> sessions) {

  @Builder
  public record Session(
      Long sessionId,
      LocalDate sessionDate,
      Map<SessionChangeType, Boolean> isSubmit,
      Integer roundNumber) {}

  public static List<ParentSessionResponse> of(
      Map<ClassSession, Pair<ClassManagement, List<SessionChangeForm>>> sessionsMap) {
    if (sessionsMap.isEmpty()) {
      return Collections.emptyList();
    }
    List<ClassSession> rawSessions = new ArrayList<>(sessionsMap.keySet());

    Map<ClassMatching, List<ClassSession>> groupedByMatching =
        rawSessions.stream()
            .collect(Collectors.groupingBy(cs -> cs.getClassManagement().getClassMatching()));

    Map<Long, List<SessionChangeForm>> changeFormMap =
        sessionsMap.entrySet().stream()
            .flatMap(entry -> entry.getValue().getRight().stream())
            .collect(
                Collectors.groupingBy(
                    form -> form.getLastSessionBeforeChange().getClassSessionId()));

    Map<ClassMatching, ClassManagement> matchingToManagement =
        sessionsMap.values().stream()
            .map(Pair::getLeft)
            .collect(Collectors.toMap(
                ClassManagement::getClassMatching,
                management -> management,
                (existing, replacement) -> existing
            ));

    List<ParentSessionResponse> result = new ArrayList<>();

    for (Map.Entry<ClassMatching, List<ClassSession>> entry : groupedByMatching.entrySet()) {
      ClassMatching matching = entry.getKey();
      List<ClassSession> sessions = entry.getValue();
      ClassManagement management = matchingToManagement.get(matching);

      if (management == null) {
        continue;
      }

      int maxRoundNumber = management.maxRoundNumber();

      List<ClassSession> calculatedSessions =
          new ClassSessions(sessions).calculateRoundWithReset(maxRoundNumber);

      List<Session> sessionResponses =
          calculatedSessions.stream()
              .map(
                  it -> {
                    List<SessionChangeForm> forms = changeFormMap.get(it.getClassSessionId());
                    Map<SessionChangeType, Boolean> submits =
                        Arrays.stream(SessionChangeType.values())
                            .collect(
                                Collectors.toMap(
                                    type -> type,
                                    type ->
                                        forms != null
                                            && forms.stream()
                                                .anyMatch(form -> form.getChangeType() == type)));

                    return Session.builder()
                        .sessionId(it.getClassSessionId())
                        .sessionDate(it.getSessionDate())
                        .isSubmit(submits)
                        .roundNumber(it.getRound())
                        .build();
                  })
              .toList();

      ParentSessionResponse response =
          ParentSessionResponse.builder()
              .applicationFormId(matching.getApplicationForm().getApplicationFormId())
              .teacherNickname(matching.getTeacher().getTeacherInfo().getNickName())
              .sessions(sessionResponses)
              .build();

      result.add(response);
    }

    return result;
  }
}
