package com.yedu.backend.domain.matching.domain.entity;


import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
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
public class ClassMatching extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classMatchingId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;
    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationForm applicationForm;
    private boolean response;
    private int responseTime;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MatchingStatus matchStatus = MatchingStatus.대기;
    private String refuseReason;

    public boolean isWaiting() {
        return matchStatus == MatchingStatus.대기;
    }

    public void updateRefuse(String refuseReason) {
        this.matchStatus = MatchingStatus.거절;
        this.refuseReason = refuseReason;
        this.response = true;
    }

    public void updateAccept() {
        this.matchStatus = MatchingStatus.수락;
        this.response = true;
    }

    public void updateSend() {
        this.matchStatus = MatchingStatus.전송;
    }

    public void startSchedule(){
        this.matchStatus = MatchingStatus.매칭;
    }

    public void confirmSchedule(){
        this.matchStatus = MatchingStatus.최종매칭;
    }

    public void refuseSchedule(String refuseReason){
        this.matchStatus = MatchingStatus.과외결렬;
        this.refuseReason = refuseReason;
    }

    public boolean isScheduleConfirm() {
        return this.matchStatus == MatchingStatus.최종매칭;
    }
}
