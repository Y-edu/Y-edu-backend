package com.yedu.backend.admin.domain.service;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import org.springframework.stereotype.Service;

@Service
public class AdminUpdateService {
    public void updateKakaoName(Parents parents, String kakaoName) {
        parents.updateKakaoName(kakaoName);
    }

    public void updateProceedStatus(ApplicationForm applicationForm) {
        applicationForm.updateProceedStatus();
    }

    public void updateTeacherIssue(Teacher teacher, String issue) {
        teacher.updateIssue(issue);
    }
}
