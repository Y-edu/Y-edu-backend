package com.yedu.api.domain.matching.domain.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

/** 과외 일정 일급 collection */
@RequiredArgsConstructor
public class ClassSessions {

  private final List<ClassSession> sessions;

  /**
   * 회차 정보 계산
   *
   * @param maxRound 최대 회차
   * @return 회차 정보가 추가된 과외 일정
   */
  public List<ClassSession> calculateRoundWithReset(Integer maxRound) {
    List<ClassSession> sorted =
        sessions.stream().sorted(Comparator.comparing(ClassSession::getSessionDate)).toList();

    List<ClassSession> sessionWithRound = new ArrayList<>();
    // todo 리팩토링
    int lastRound = 0;

    for (ClassSession session : sorted) {
      if (session.isCancel() || session.isTodayCancel()) {
        sessionWithRound.add(session);
        continue;
      }

      if (session.isCompleted()) {
        // 완료된 과외 → 기존 회차 유지
        if (session.getRound() != null) {
          lastRound = session.getRound();
        }
        sessionWithRound.add(session);
        continue;
      }

      // 미완료 → 새 회차
      int nextRound = lastRound + 1;
      if (nextRound > maxRound) {
        nextRound = 1;
      }
      lastRound = nextRound;

      sessionWithRound.add(session.withRound(nextRound));
    }

    return sessionWithRound;
  }
}
