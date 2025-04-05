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
    private TeacherStatus status = TeacherStatus.등록폼작성완료;
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
    @Column(nullable = false)
    private int refuseCount;
    @Column(nullable = false)
    private boolean remind;

    private int classCount; //수업 횟수
    private int alertMessageCount; //알림톡 발송 횟수

    private int responseCount; // 응답 횟수

    private int totalRequestCount; // 전체 요청 횟수

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
    public void updateActive() {
        this.status = TeacherStatus.활동중;
    }

    public void updateStep() {
        this.status = TeacherStatus.사진영상제출완료;
    }
    public void updateStatusByAlarmTalk(boolean alarmTalk) {
        if (alarmTalk) {
            this.status = TeacherStatus.활동중;
            return;
        }
        this.status = TeacherStatus.일시정지;
    }

    public boolean isActive() {
        if (this.status == TeacherStatus.활동중 || this.status == TeacherStatus.일시정지)
            return true;
        return false;
    }

    public void plusRefuseCount() {
        this.refuseCount++;
    }

    public void clearRefuseCount() {
        this.refuseCount = 0;
    }

    public void updateRemind() {
        this.remind = true;
    }

    public void plusRequestCount() {
        this.totalRequestCount++;
    }

    public void plusResponseCount() {
        this.responseCount++;
    }
}
