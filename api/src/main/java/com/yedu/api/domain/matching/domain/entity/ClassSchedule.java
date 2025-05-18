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
import java.util.Collection;
import java.util.Map;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_management_id")
  private ClassManagement classManagement;

  @Enumerated(EnumType.STRING)
  private Day day;

  @Embedded private ClassTime classTime;

  public boolean contains(ClassSession session) {
    Day day = Day.byDate(session.getSessionDate());

    return session.getClassTime().equals(this.classTime) && day.equals(this.day);
  }

  /***
   * 새로운 과외 일정을 생성합니다.
   */
  public Collection<ClassSession> generateUpcomingDates(
      ClassManagement classManagement, LocalDate today, Map<LocalDate, ClassSession> existingSessionMap) {
    LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

    return Stream.iterate(today, date -> !date.isAfter(endOfMonth), date -> date.plusDays(1))
        .filter(date -> Day.byDate(date).equals(this.day))
        .filter(it-> !existingSessionMap.containsKey(it))
        .map(
            date ->
                ClassSession.builder()
                    .classManagement(classManagement)
                    .sessionDate(date)
                    .classTime(this.classTime)
                    .completed(false)
                    .cancel(false)
                    .build())
        .toList();
  }
}
