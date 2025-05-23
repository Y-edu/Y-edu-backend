package com.yedu.api.domain.parents.domain.entity;

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
public class Goal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long goalId;

  @ManyToOne(fetch = FetchType.EAGER)
  private ApplicationForm applicationForm;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String classGoal;
}
