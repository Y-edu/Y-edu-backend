package com.yedu.api.domain.parents.domain.entity;

import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.api.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class ApplicationFormAvailable extends BaseEntity {

  @Id
  @Column(name = "available_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Day day;

  @Column(nullable = false)
  private LocalTime availableTime;

  @Column(nullable = false, name = "application_form_application_form_id")
  private String applicationFormId;
}
