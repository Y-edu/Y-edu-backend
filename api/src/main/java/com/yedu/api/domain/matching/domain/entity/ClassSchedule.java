package com.yedu.api.domain.matching.domain.entity;

import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.api.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassSchedule extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long classScheduleId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "class_management_id")
  private ClassManagement classManagement;

  @Enumerated(EnumType.STRING)
  private Day day;

  @Embedded private ClassTime classTime;

  // 클래스 레벨로 이동하여 중복 생성 방지
  private static final AtomicInteger globalTeacherRoundCounter = new AtomicInteger(1);

  public boolean contains(ClassSession session) {
    Day day = Day.byDate(session.getSessionDate());

    return session.getClassTime().equals(this.classTime) && day.equals(this.day);
  }

  /***
   * 이번달에 해당되는 새로운 과외 일정을 생성합니다.
   */
  public Collection<ClassSession> generateUpcomingDates(
      ClassManagement classManagement,
      LocalDate today,
      Map<LocalDate, ClassSession> existingSessionMap,
      LocalDate changeStartDate) {
    LocalDate classStartDate =
        Optional.ofNullable(changeStartDate)
            .or(
                () ->
                    Optional.ofNullable(classManagement.getFirstDay())
                        .map(firstDay -> firstDay.isBefore(today) ? today : firstDay))
            .orElse(today);
    LocalDate lastDay = today.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
    Integer maxRound = calculateMaxRound(classManagement);

    // 새로 생성될 날짜들을 수집하여 정렬
    List<LocalDate> newDates =
        Stream.iterate(classStartDate, date -> !date.isAfter(lastDay), date -> date.plusDays(1))
            .filter(date -> Day.byDate(date).equals(this.day))
            .filter(it -> !existingSessionMap.containsKey(it))
            .sorted()
            .toList();

    if (newDates.isEmpty()) {
      return List.of();
    }

    // 새로 생성되는 날짜들에서 월별로 teacherRound 관리
    AtomicInteger teacherRoundCounter = new AtomicInteger(1);
    AtomicReference<LocalDate> currentMonthRef = new AtomicReference<>();

    return newDates.stream()
        .map(
            date -> {
              // 월이 바뀌면 teacherRound를 1로 리셋
              LocalDate currentMonth = currentMonthRef.get();
              if (currentMonth == null
                  || date.getMonth() != currentMonth.getMonth()
                  || date.getYear() != currentMonth.getYear()) {
                teacherRoundCounter.set(1);
                currentMonthRef.set(date);
              }

              // 현재 월 내에서 순차적으로 증가
              int currentRound = teacherRoundCounter.getAndIncrement();

              return ClassSession.builder()
                  .classManagement(classManagement)
                  .sessionDate(date)
                  .classTime(this.classTime)
                  .completed(false)
                  .cancel(false)
                  .remind(false)
                  .teacherRound(currentRound)
                  .maxRound(maxRound)
                  .build();
            })
        .toList();
  }

  private Integer calculateMaxRound(ClassManagement classManagement) {
    String classCount = classManagement.getClassMatching().getApplicationForm().getClassCount();

    return switch (classCount) {
      case String count when count.contains("주 1회") -> 4;
      case String count when count.contains("주 2회") -> 8;
      case String count when count.contains("주 3회") -> 12;
      case String count when count.contains("주 4회") -> 16;
      case String count when count.contains("주 5회") -> 20;
      case String count when count.contains("주 6회") -> 24;
      case String count when count.contains("주 7회") -> 28;
      default -> 4;
    };
  }
}
