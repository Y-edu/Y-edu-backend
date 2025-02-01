package com.yedu.backend.domain.parents.domain.entity;

import com.yedu.backend.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private long parentsId;
    private int count;
    private String kakaoName;
    @Column(nullable = false)
    private String phoneNumber;
    private int totalPayment;
    @Builder.Default
    private boolean marketingAgree = true;
}
