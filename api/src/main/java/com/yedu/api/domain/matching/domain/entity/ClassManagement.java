package com.yedu.api.domain.matching.domain.entity;

import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassManagement extends BaseEntity {

  private static final int ROUND_TIMES = 4;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long classManagementId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "class_matching_id")
  private ClassMatching classMatching;

  @OneToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.ALL},
      orphanRemoval = true,
      mappedBy = "classManagement")
  private List<ClassSchedule> schedules = new ArrayList<>();

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST},
      mappedBy = "classManagement")
  private List<ClassScheduleHistory> scheduleHistories = new ArrayList<>();


  private String textbook;

  private LocalDate firstDay;

  @Embedded private ClassTime classTime;

  private boolean remind;

  public void refuse(String reason) {
    classMatching.refuseSchedule(reason);
  }

  public void addSchedule(ClassSchedule schedule) {
    if (schedules == null) {
      schedules = new ArrayList<>();
    }
    schedules.add(schedule);
  }

  public void confirm(String textbook, LocalDate firstDay, ClassTime classTime) {
    this.textbook = textbook;
    this.firstDay = firstDay;
    this.classTime = classTime;
    this.classMatching.confirmSchedule();
  }

  public void completeRemind() {
    this.remind = true;
  }

  public void resetSchedule(LocalDate appliedAt) {
    if (CollectionUtils.isEmpty(schedules)) {
      return;
    }
    for (ClassSchedule schedule : schedules) {
      ClassScheduleHistory history = ClassScheduleHistory.builder()
          .classManagement(this)
          .day(schedule.getDay())
          .classTime(schedule.getClassTime())
          .appliedAt(appliedAt)
          .build();
      scheduleHistories.add(history);
    }
    schedules.clear();
  }


  public boolean hasSchedule() {
    return !CollectionUtils.isEmpty(schedules);
  }


  public int maxRoundNumber() {
    if (CollectionUtils.isEmpty(schedules)) {
      return 0;
    }

    List<ClassScheduleHistory> latestHistories = getRecentScheduleHistories();
    if (latestHistories.isEmpty()) {
      return schedules.size() * ROUND_TIMES;
    }

    return latestHistories.size() * ROUND_TIMES;
  }

  public int totalClassMinute() {
    if (CollectionUtils.isEmpty(schedules)) {
      return 0;
    }

    List<ClassScheduleHistory> latestHistories = getRecentScheduleHistories();
    if (latestHistories.isEmpty()) {
      return schedules.stream()
          .mapToInt(s -> s.getClassTime().getClassMinute())
          .sum();
    }

    return latestHistories.stream()
        .mapToInt(h -> h.getClassTime().getClassMinute())
        .sum();
  }


  public int getMaxClassMinute() {
    if (CollectionUtils.isEmpty(schedules)) {
      return 0;
    }

    List<ClassScheduleHistory> latestHistories = getRecentScheduleHistories();
    if (latestHistories.isEmpty()) {
      return schedules.stream()
          .mapToInt(s -> s.getClassTime().getClassMinute())
          .max()
          .orElse(0);
    }

    return latestHistories.stream()
        .mapToInt(h -> h.getClassTime().getClassMinute())
        .max()
        .orElse(0);
  }

    /**
     * 지나간 가까운 날짜의 스케줄 히스토리 목록을 가져옵니다.
     */
  private List<ClassScheduleHistory> getRecentScheduleHistories() {
    if (CollectionUtils.isEmpty(scheduleHistories)) {
      return Collections.emptyList();
    }

    LocalDate now = LocalDate.now();

    Optional<LocalDate> upcomingDateOpt = scheduleHistories.stream()
        .map(ClassScheduleHistory::getAppliedAt)
        .filter(Objects::nonNull)
        .filter(date -> !date.isBefore(now))
        .min(Comparator.naturalOrder());

    if (upcomingDateOpt.isEmpty()) {
      return Collections.emptyList();
    }

    LocalDate upcomingDate = upcomingDateOpt.get();

    return scheduleHistories.stream()
        .filter(h -> upcomingDate.equals(h.getAppliedAt()))
        .toList();
  }


  public int monthClassMinute() {
    if (CollectionUtils.isEmpty(schedules)) {
      return 0;
    }
    return maxRoundNumber() * totalClassMinute();
  }
}
