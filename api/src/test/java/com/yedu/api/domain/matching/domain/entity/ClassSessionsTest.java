package com.yedu.api.domain.matching.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ClassSessionsTest {

  static Stream<TestCase> sessionCases() {
    LocalDate base = LocalDate.of(2025, 8, 1);

    return Stream.of(
        // 미완료 + 정상 → 회차 부여됨 (1)
        new TestCase(
            List.of(newSession(base.plusDays(1), false, false, false, null)), 3, List.of(1)),
        // 완료된 과외 있음 → 다음은 이어서 (2)
        new TestCase(
            List.of(
                newSession(base.plusDays(1), true, false, false, 1),
                newSession(base.plusDays(2), false, false, false, null)),
            3,
            List.of(1, 2)),
        // maxRound 초과 → reset
        new TestCase(
            List.of(
                newSession(base.plusDays(1), true, false, false, 3),
                newSession(base.plusDays(2), false, false, false, null)),
            3,
            List.of(3, 1)),
        // 휴강 → 회차 제외
        new TestCase(
            List.of(
                newSession(base.plusDays(1), false, true, false, null),
                newSession(base.plusDays(2), false, false, false, null)),
            3,
            Arrays.asList(null, 1)),
        // 당일휴강 → 회차 제외
        new TestCase(
            List.of(
                newSession(base.plusDays(1), false, false, true, null),
                newSession(base.plusDays(2), false, false, false, null)),
            3,
            Arrays.asList(null, 1)));
  }

  @ParameterizedTest
  @MethodSource("sessionCases")
  void calculateRoundWithReset_should_assign_correct_rounds(TestCase testCase) {
    // given
    ClassSessions classSessions = new ClassSessions(testCase.sessions);

    // when
    List<ClassSession> result = classSessions.calculateRoundWithReset(testCase.maxRound);

    // then
    List<Integer> actualRounds = result.stream().map(ClassSession::getRound).toList();

    assertThat(actualRounds).isEqualTo(testCase.expectedRounds);
  }

  private static ClassSession newSession(
      LocalDate date, boolean completed, boolean cancel, boolean todayCancel, Integer round) {
    return ClassSession.builder()
        .sessionDate(date)
        .completed(completed)
        .cancel(cancel)
        .isTodayCancel(todayCancel)
        .round(round)
        .build();
  }

  record TestCase(List<ClassSession> sessions, int maxRound, List<Integer> expectedRounds) {}

  @Test
  void foo(){
    String format = LocalDate.now().format(DateTimeFormatter.ofPattern("mm/dd"));
    System.out.println("format = " + format);
  }
}
