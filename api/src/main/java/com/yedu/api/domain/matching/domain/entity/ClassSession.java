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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 실제 수업 정보 기록
 */
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

  private String understandingAndAttitude;

  private boolean cancel;

  private String cancelReason;

  private boolean completed;

  @Embedded private ClassTime classTime;


  public boolean isUpcoming(){
    return (!cancel && cancelReason == null) && !completed;
  }
}
