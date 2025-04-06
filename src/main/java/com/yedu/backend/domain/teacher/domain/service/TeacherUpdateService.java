package com.yedu.backend.domain.teacher.domain.service;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import org.springframework.stereotype.Service;

@Service
public class TeacherUpdateService {
    public void updateProfile(Teacher teacher, String profile) {
        teacher.updateProfile(profile);
    }
    public void updateFormStep(Teacher teacher) {
        teacher.updateStep();
    }
    public void updateAlertCount(Teacher teacher) {
        teacher.updateMessageCount();
    }
    public void updateActive(Teacher teacher) {
        teacher.updateActive();
    }
    public void updateStatus(Teacher teacher, boolean alarmTalk) {
        teacher.updateStatusByAlarmTalk(alarmTalk);
    }
    public void plusRefuseCount(Teacher teacher) {
        teacher.plusRefuseCount();
    }
    public void clearRefuseCount(Teacher teacher) {
        teacher.clearRefuseCount();
    }
    public void updateRemind(Teacher teacher) {
        teacher.updateRemind();
    }

    public void plusResponseCount(Teacher teacher) {
        teacher.plusResponseCount();
    }

    public void plusRequestCount(Teacher teacher) {
        teacher.plusRequestCount();
    }
}
