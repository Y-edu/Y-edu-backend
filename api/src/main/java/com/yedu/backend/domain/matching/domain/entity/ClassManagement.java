package com.yedu.backend.domain.matching.domain.entity;

import com.yedu.backend.domain.matching.domain.vo.ClassTime;
import com.yedu.backend.global.entity.BaseEntity;
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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassManagement extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long classManagementId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_matching_id")
  private ClassMatching classMatching;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      mappedBy = "classManagement")
  private List<ClassSchedule> schedules = new ArrayList<>();

  private String textbook;

  private LocalDate firstDay;

  @Embedded private ClassTime classTime;

  private boolean remind;

  public void refuse(String reason) {
    classMatching.refuseSchedule(reason);
  }

  public void addSchedule(ClassSchedule schedule) {
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
}
