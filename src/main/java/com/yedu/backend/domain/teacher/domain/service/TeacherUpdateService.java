package com.yedu.backend.domain.teacher.domain.service;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import org.springframework.stereotype.Service;

@Service
public class TeacherUpdateService {
    public void updateProfile(Teacher teacher, String profile) {
        teacher.updateProfile(profile);
    }
    public void updateAlertCount(Teacher teacher) {
        teacher.updateMessageCount();
    }
    public void updateActive(Teacher teacher) {
        teacher.updateActive();
    }
}
