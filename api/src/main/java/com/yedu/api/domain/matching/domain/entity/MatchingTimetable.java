package com.yedu.api.domain.matching.domain.entity;

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
public class MatchingTimetable extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long timetableId;

  @Column(nullable = false)
  private LocalTime timetableTime;

  @Enumerated(EnumType.STRING)
  private Day day;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_matching_id")
  private ClassMatching classMatching;
}
