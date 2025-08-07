package com.yedu.api.domain.matching.domain.entity;

import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.matching.domain.entity.constant.CancelReason;
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
import lombok.ToString;
import lombok.With;

/** 실제 수업 정보 기록 */
@ToString
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

  private String homework;

  private boolean cancel;

  private String cancelReason;

  private boolean completed;

  private boolean remind;

  @Embedded private ClassTime classTime;

  private Integer realClassTime;

  @With
  private Integer round; // 회차

  private boolean isTodayCancel;

  private Integer teacherRound;

  public void cancel(String cancelReason, boolean isTodayCancel) {
    if (cancel) {
      throw new IllegalStateException("이미 취소된 일정입니다");
    }
    if (completed) {
      throw new IllegalStateException("완료된 일정입니다");
    }
    ClassMatching classMatching = classManagement.getClassMatching();
    MatchingStatus matchStatus = classMatching.getMatchStatus();

    boolean isStoppedBeforeSession =
        (matchStatus == MatchingStatus.중단 || matchStatus == MatchingStatus.일시중단)
            && classMatching.getPausedAt() != null
            && !classMatching.getPausedAt().toLocalDate().isAfter(sessionDate);

    if (matchStatus != MatchingStatus.최종매칭 && isStoppedBeforeSession) {
      throw new IllegalStateException("최종 매칭된 과외가 아니거나, 중단된 과외입니다");
    }

    cancel = true;
    this.isTodayCancel = isTodayCancel || false;
    this.cancelReason = cancelReason;
  }

  public void revertCancel() {
    if (!cancel) {
      throw new IllegalStateException("취소되지 않은 일정입니다");
    }
    cancel = false;
    this.cancelReason = null;
  }

  public void complete(Integer realClassMinute, String understanding, String homework) {
    if (cancel) {
      throw new IllegalStateException("취소된 일정입니다");
    }
    if (completed) {
      throw new IllegalStateException("이미 완료된 일정입니다");
    }
    ClassMatching classMatching = classManagement.getClassMatching();
    MatchingStatus matchStatus = classMatching.getMatchStatus();

    boolean isStoppedBeforeSession =
        (matchStatus == MatchingStatus.중단 || matchStatus == MatchingStatus.일시중단)
            && classMatching.getPausedAt() != null
            && !classMatching.getPausedAt().toLocalDate().isAfter(sessionDate);

    if (matchStatus != MatchingStatus.최종매칭 && isStoppedBeforeSession) {
      throw new IllegalStateException("최종 매칭된 과외가 아니거나, 중단된 과외입니다");
    }

    this.completed = true;
    this.realClassTime = realClassMinute;
    this.homework = homework;
    this.understanding = understanding;
  }

  public void changeDate(LocalDate sessionDate, LocalTime start) {
    this.sessionDate = sessionDate;
    this.classTime = this.classTime.withStart(start);
  }

  public boolean isFinishAndNotComplete(LocalDateTime time, boolean isRemind) {
    if (completed) {
      return false;
    }
    if (cancel) {
      return false;
    }
    // 리마인드 알림톡은 수업 종료 이후 1시간 뒤 발송되야함
    if (isRemind) {
      return LocalDateTime.of(sessionDate, classTime.finishTime()).plusHours(1L).isBefore(time);
    }
    return LocalDateTime.of(sessionDate, classTime.finishTime()).isBefore(time);
  }

  public void remind() {
    this.remind = true;
  }

  public void complete(Integer realClassMinute, String understanding, String homework, Integer round) {
    if (round != null){
      this.round = round;
    }
    this.complete(realClassMinute, understanding, homework);
  }
}
