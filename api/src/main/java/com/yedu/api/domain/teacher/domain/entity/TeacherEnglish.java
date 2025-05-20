package com.yedu.api.domain.teacher.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherEnglish {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long englishId;

  @ManyToOne(fetch = FetchType.EAGER)
  private Teacher teacher;

  @Column(columnDefinition = "TEXT")
  private String appealPoint;

  @Column(columnDefinition = "TEXT")
  private String teachingExperience;

  @Column(nullable = false)
  private int teachingHistory;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String teachingStyle;

  @Column(columnDefinition = "TEXT")
  private String managementStyle;

  @Column(columnDefinition = "TEXT")
  private String foreignExperience;
}
