package com.yedu.backend.domain.teacher.domain.entity;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeachingStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class TeacherClassInfo {
    @Column(columnDefinition = "TEXT", nullable = false)
    private String introduce; //소개
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeachingStyle teachingStyle1;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeachingStyle teachingStyle2;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String teachingStyleInfo1;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String teachingStyleInfo2;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String recommendStudent;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment; //하고싶은 말
    @Column(nullable = false)
    private boolean englishPossible;
    @Column(nullable = false)
    private boolean mathPossible;

    private String profile; //프로필 사진
    private String video; // 과외 영상

    public void updateProfile(String profile) {
        this.profile = profile;
        //todo : 나중에 과외 영상 추가 필요
    }
}
