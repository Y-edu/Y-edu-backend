package com.yedu.api.domain.matching.domain.entity;

import com.yedu.api.domain.matching.domain.vo.ClassTime;
import com.yedu.api.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 실제 수업 정보 기록 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassSession extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long classSessionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_management_id")
  private ClassManagement classManagement;

  private LocalDate sessionDate;

  private String understanding;

  private Integer homeworkPercentage;

  private boolean cancel;

  private String cancelReason;

  private boolean completed;

  @Embedded private ClassTime classTime;

  public boolean isUpcoming() {
    return (!cancel && cancelReason == null) && !completed;
  }

  public void cancel(String cancelReason) {
    if (cancel) {
      throw new IllegalStateException("이미 취소된 일정입니다");
    }
    cancel = true;
    this.cancelReason = cancelReason;
  }

  public void revertCancel() {
    if (!cancel) {
      throw new IllegalStateException("취소되지 않은 일정입니다");
    }
    cancel = false;
    this.cancelReason = null;
  }

  public void complete(String understanding, Integer homeworkPercentage) {
    if (completed) {
      throw new IllegalStateException("이미 완료된 일정입니다");
    }
    completed = true;
    this.homeworkPercentage = homeworkPercentage;
    this.understanding = understanding;
  }

  public void changeDate(LocalDate sessionDate, LocalTime start) {
    this.sessionDate = sessionDate;
    this.classTime = this.classTime.withStart(start);
  }

  public boolean isFinish(LocalDateTime time) {
    return sessionDate.equals(time.toLocalDate())
        && this.classTime.finishTime().isBefore(time.toLocalTime());
  }
}
