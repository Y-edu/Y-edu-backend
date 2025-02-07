package com.yedu.backend.domain.teacher.domain.entity;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGrade;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherStatus;
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
public class Teacher extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teacherId;

    @Embedded
    private TeacherInfo teacherInfo;

    @Embedded
    private TeacherSchoolInfo teacherSchoolInfo;

    @Embedded
    private TeacherClassInfo teacherClassInfo;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TeacherStatus status = TeacherStatus.활동중; //활동 상태 (관리자 수락 대기)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TeacherGrade grade = TeacherGrade.STANDARD; //등급

    @Column(columnDefinition = "TEXT")
    private String issue;
    @Column(columnDefinition = "TEXT")
    private String terminateIssue;
    @Column(nullable = false)
    private String source; //유입경로
    @Column(nullable = false)
    private boolean marketingAgree;

    private int classCount; //수업 횟수
    private int alertMessageCount; //알림톡 발송 횟수
    private double responseRate; //답변율
    private double responseTime; //답변 평균 시간

    public void updateProfile(String profile) {
        teacherInfo.updateProfile(profile);
    }

    public void updateVideo(String video) {
        teacherInfo.updateVideo(video);
    }

    public void updateMessageCount() {
        this.alertMessageCount++;
    }
    public void updateIssue(String issue) {
        this.issue = issue;
    }
}
