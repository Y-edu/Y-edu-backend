package com.yedu.api.admin.domain.entity;

import com.yedu.api.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Event  {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private Class<?> eventType;

  private String payload;
}
