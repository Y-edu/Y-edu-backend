package com.yedu.api.domain.teacher.domain.entity;

import com.yedu.api.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherAvailable extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long availableId;

  @Column(nullable = false)
  private LocalTime availableTime;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Day day;

  @ManyToOne(fetch = FetchType.EAGER)
  private Teacher teacher;

  public boolean isSameTo(ApplicationFormAvailable applicationFormAvailable) {
    return applicationFormAvailable.getAvailableTime().equals(availableTime)
        && applicationFormAvailable.getDay().equals(day);
  }
}
