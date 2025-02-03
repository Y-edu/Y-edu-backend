package com.yedu.backend.domain.parents.domain.entity;

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
public class Parents extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long parentsId;
    @Column(nullable = false)
    private int count;
    private String kakaoName;
    @Column(nullable = false)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private District district;
    @Column(nullable = false)
    private String dong;
    @Column(nullable = false)
    private int totalPayment;
    @Builder.Default
    private boolean marketingAgree = true;

    public void updateKakaoName(String kakaoName) {
        this.kakaoName = kakaoName;
    }

    public void updateCount() {
        this.count++;
    }
}
