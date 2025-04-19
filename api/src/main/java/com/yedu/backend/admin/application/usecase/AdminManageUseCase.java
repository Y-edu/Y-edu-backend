package com.yedu.backend.admin.application.usecase;

import com.yedu.backend.admin.application.dto.req.ParentsKakaoNameRequest;
import com.yedu.backend.admin.application.dto.req.ProposalTeacherRequest;
import com.yedu.backend.admin.application.dto.req.RecommendTeacherRequest;
import com.yedu.backend.admin.application.dto.req.TeacherIssueRequest;
import com.yedu.backend.admin.application.dto.req.TeacherVideoRequest;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.admin.domain.service.AdminSaveService;
import com.yedu.backend.admin.domain.service.AdminUpdateService;
import com.yedu.backend.admin.domain.service.ResponseRateStorage;
import com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.backend.global.event.publisher.EventPublisher;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yedu.backend.global.event.mapper.BizppurioEventMapper.*;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminManageUseCase {
    private final AdminGetService adminGetService;
    private final AdminUpdateService adminUpdateService;
    private final AdminSaveService adminSaveService;
    private final ResponseRateStorage responseRateStorage;
    private final TeacherUpdateService teacherUpdateService;
    private final EventPublisher eventPublisher;

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

    public void recommendTeacher(RecommendTeacherRequest request) { //todo : 알림톡 링크 수정 필요 (토큰 활용)
        List<RecommendTeacherEvent> recommendTeacherEvents = request.classMatchingIds()
                .stream()
                .map(id -> {
                    ClassMatching classMatching = adminGetService.classMatchingById(id);
                    adminUpdateService.updateClassMatchingSend(classMatching);
                    return mapToRecommendTeacherEvent(classMatching);
                })
                .toList();
        recommendTeacherEvents.forEach(eventPublisher::publishRecommendTeacherEvent);
        RecommendTeacherEvent recommendTeacherEvent = recommendTeacherEvents.get(0);
        eventPublisher.publishRecommendGuideEvent(mapToRecommendGuideEvent(recommendTeacherEvent.parentsPhoneNumber()));
    }

    public void proposalTeacher(String applicationFormId, ProposalTeacherRequest request) {
        ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
        request.teacherIds().forEach(id -> {
                    Teacher teacher = adminGetService.teacherById(id);
                    teacherUpdateService.plusRequestCount(teacher);
                    responseRateStorage.cache(teacher.getTeacherId());

                    ClassMatching classMatching = ClassMatchingMapper.mapToClassMatching(teacher, applicationForm);
                    adminSaveService.saveClassMatching(classMatching);
                    adminUpdateService.updateAlertCount(teacher);
                    eventPublisher.publishNotifyClassInfoEvent(mapToNotifyClassInfoEvent(classMatching));
                });
    }
}
