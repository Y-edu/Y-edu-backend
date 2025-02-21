package com.yedu.backend.domain.teacher.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class TeacherSchoolInfo {
    //대학교 정보
    @Column(nullable = false)
    private String university;
    @Column(nullable = false)
    @Builder.Default
    private boolean etc = false;
    @Column(nullable = false)
    private String major;
    //고등학교 정보
    @Column(nullable = false)
    private String highSchool;
    private String highSchoolType;
}
