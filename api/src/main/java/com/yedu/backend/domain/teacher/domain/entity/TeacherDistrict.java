package com.yedu.backend.domain.teacher.domain.entity;

import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.global.entity.BaseEntity;
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
public class TeacherDistrict extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long districtId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private District district;

  @ManyToOne(fetch = FetchType.LAZY)
  private Teacher teacher;
}
