package com.yedu.backend.domain.parents.domain.entity;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Gender;
import com.yedu.backend.domain.parents.domain.entity.constant.Level;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;
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
public class ApplicationForm extends BaseEntity {
    @Id
    private String applicationFormId; // 지역구 + 학부모PK + 학부모 횟수(a~z)
    @ManyToOne(fetch = FetchType.LAZY)
    private Parents parents;
    @Column(nullable = false)
    private String age; // 아이나이
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Online online; // 대면 비대면
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private District district;
    @Column(nullable = false)
    private String dong;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClassType wantedSubject; // 수학 혹은 영어
    @Enumerated(EnumType.STRING)
    private Level level;
    @Column(columnDefinition = "TEXT")
    private String favoriteCondition; // 원하는 선생님 요건
    @Enumerated(EnumType.STRING)
    private Level educationImportance; // 학벌 중요도
    @Column(nullable = false, columnDefinition = "TEXT")
    private String favoriteStyle; //선생님 스타일
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender favoriteGender;
    @Column(columnDefinition = "TEXT")
    private String favoriteDirection; //수업 방향성
    @Column(nullable = false, length = 90)
    private String wantTime; //수업 날짜
    @Column(nullable = false)
    private String classCount; // 몇회 (1,2,3)
    @Column(nullable = false)
    private String classTime; // 수업 시간(50, 75, 100...)
    private String source; //유입경로
    @Builder.Default
    @Column(nullable = false)
    private boolean proceedStatus = false; // 처리 상태

    public void updateProceedStatus() {
        proceedStatus = !proceedStatus;
    }
}
