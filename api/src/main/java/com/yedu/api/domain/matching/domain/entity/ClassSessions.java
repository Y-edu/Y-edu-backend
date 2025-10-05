package com.yedu.api.domain.matching.domain.entity;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

/** 과외 일정 일급 collection */
@RequiredArgsConstructor
public class ClassSessions {

  private static final long TEACHER_PER_MINUTE_FEE = 600L;

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

  public String historyMessage() {
    String histories = sessions.stream()
        .sorted(Comparator.comparing(ClassSession::getSessionDate))
        .map(it ->
            it.getSessionDate().format(DateTimeFormatter.ofPattern("MM/dd")) +
                " " + it.getRealClassTime() +
                " 분 " +
                it.getRound() +
                " 회차 완료"
        )
        .collect(Collectors.joining("\n"));

    return histories;
  }

  public Integer sumClassMinutes(){
    return sessions.stream().mapToInt(ClassSession::getRealClassTime).sum();
  }

  public BigDecimal fee(){
    return BigDecimal.valueOf(sumClassMinutes() * TEACHER_PER_MINUTE_FEE);
  }


  public String paymentCallbackUrl(String serverUrl){
    return serverUrl + "/sessions/" + sessions.stream()
        .map(ClassSession::getClassSessionId)
        .map(String::valueOf)
        .collect(Collectors.joining(",")) + "/pay";
  }

  public void payPending() {
    sessions.forEach(ClassSession::payPending);
  }

  public void numberRound(Integer maxRoundNumber) {
    List<ClassSession> orderedSessions = sessions.stream()
        .sorted(Comparator.comparing(ClassSession::getSessionDate))
        .toList();

    IntStream.range(0, orderedSessions.size())
        .forEach(i -> orderedSessions.get(i).updateRound(i, maxRoundNumber));
  }
}
