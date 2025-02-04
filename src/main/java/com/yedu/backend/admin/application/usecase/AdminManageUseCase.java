package com.yedu.backend.admin.application.usecase;

import com.yedu.backend.admin.application.dto.req.ParentsKakaoNameRequest;
import com.yedu.backend.admin.application.dto.req.TeacherIssueRequest;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.admin.domain.service.AdminUpdateService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AdminManageUseCase {
    private final AdminGetService adminGetService;
    private final AdminUpdateService adminUpdateService;

    public void updateParentsKakaoName(long parentsId, ParentsKakaoNameRequest request) {
        Parents parents = adminGetService.parentsById(parentsId);
        adminUpdateService.updateKakaoName(parents, request.kakaoName());
    }

    public void updateProceedStatus(String applicationFormId) {
        ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
        adminUpdateService.updateProceedStatus(applicationForm);
    }

    public void updateTeacherIssue(long teacherId, TeacherIssueRequest request) {
        Teacher teacher = adminGetService.teacherById(teacherId);
        adminUpdateService.updateTeacherIssue(teacher, request.issue());
    }
}
