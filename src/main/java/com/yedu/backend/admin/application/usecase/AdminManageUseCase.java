package com.yedu.backend.admin.application.usecase;

import com.yedu.backend.admin.application.dto.req.*;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.admin.domain.service.AdminSaveService;
import com.yedu.backend.admin.domain.service.AdminUpdateService;
import com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.global.bizppurio.application.usecase.BizppurioParentsMessage;
import com.yedu.backend.global.bizppurio.application.usecase.BizppurioTeacherMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminManageUseCase {
    private final AdminGetService adminGetService;
    private final AdminUpdateService adminUpdateService;
    private final AdminSaveService adminSaveService;
    private final BizppurioParentsMessage bizppurioParentsMessage;
    private final BizppurioTeacherMessage bizppurioTeacherMessage;

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

    public void updateTeacherVideo(long teacherId, TeacherVideoRequest request) {
        Teacher teacher = adminGetService.teacherById(teacherId);
        adminUpdateService.updateTeacherVideo(teacher, request.video());
    }

    public void recommendTeacher(RecommendTeacherRequest request) {
        List<ClassMatching> classMatchings = request.classMatchingIds()
                .stream()
                .map(adminGetService::classMatchingById)
                .toList();
        classMatchings.forEach(adminUpdateService::updateClassMatchingSend);
        bizppurioParentsMessage.recommendTeacher(classMatchings);
    }

    public void proposalTeacher(String applicationFormId, ProposalTeacherRequest request) {
        ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
        request.teacherIds().forEach(id -> {
                    Teacher teacher = adminGetService.teacherById(id);
                    ClassMatching classMatching = ClassMatchingMapper.mapToClassMatching(teacher, applicationForm);
                    adminSaveService.saveClassMatching(classMatching);
                    adminUpdateService.updateAlertCount(teacher);
                    bizppurioTeacherMessage.notifyClass(applicationForm, teacher);
                });
    }
}
