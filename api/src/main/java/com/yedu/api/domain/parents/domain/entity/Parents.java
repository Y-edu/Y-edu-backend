package com.yedu.api.domain.parents.domain.entity;

import com.yedu.api.global.entity.BaseEntity;
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
public class Parents extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long parentsId;

  @Column(nullable = false)
  private int count;

  private String kakaoName;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private int totalPayment;

  @Builder.Default private boolean marketingAgree = true;

  public void updateKakaoName(String kakaoName) {
    this.kakaoName = kakaoName;
  }

  public void updateCount() {
    this.count++;
  }
}
