package com.yedu.backend.domain.parents.domain.entity;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Gender;
import com.yedu.backend.domain.parents.domain.entity.constant.Level;
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
public class Diagnosis {
    @Id
    private long DiagnosisId;
    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationForm applicationForm;
    @Column(nullable = false)
    private ClassType type;
    @Column(nullable = false)
    private Level level;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String favoriteCondition; // 원하는 선생님 요건
    @Column(nullable = false)
    private Level educationImportance;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String favoriteStyle; //선생님 스타일
    @Column(nullable = false)
    private Gender favoriteGender;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String favoriteDirection; //수업 방향성
    @Column(nullable = false, length = 90)
    private String wantTime; //수업 날짜
    @Column(nullable = false)
    private String classCount; // 몇회 (1,2,3)
    @Column(nullable = false)
    private String classTime; // 수업 시간(50, 75, 100...)
    @Column(nullable = false)
    private String source; //유입경로
}
