package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingSaveService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingUpdateService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.backend.global.event.publisher.BizppurioEventPublisher;
import com.yedu.backend.global.exception.matching.MatchingStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yedu.backend.domain.matching.application.constant.RefuseReason.UNABLE_DISTRICT;
import static com.yedu.backend.domain.matching.application.constant.RefuseReason.UNABLE_NOW;
import static com.yedu.backend.global.event.mapper.EventMapper.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassMatchingManageUseCase {
    private final ClassMatchingSaveService classMatchingSaveService;
    private final ClassMatchingGetService classMatchingGetService;
    private final ClassMatchingUpdateService classMatchingUpdateService;
    private final TeacherUpdateService teacherUpdateService;
    private final BizppurioEventPublisher bizppurioEventPublisher;

    public List<ClassMatching> saveAllClassMatching(List<Teacher> teachers, ApplicationForm applicationForm) {
        List<ClassMatching> classMatchings = teachers.stream()
                .map(teacher -> ClassMatchingMapper.mapToClassMatching(teacher, applicationForm))
                .toList();
        classMatchings.forEach(classMatchingSaveService::save);
        return classMatchings;
    }

    public void refuseClassMatching(String applicationFormId, long teacherId, String phoneNumber, ClassMatchingRefuseRequest request) {
        ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(applicationFormId, teacherId, phoneNumber);
        if (!classMatching.isWaiting())
            throw new MatchingStatusException(classMatching.getClassMatchingId());
        classMatchingUpdateService.updateRefuse(classMatching, request);
        Teacher teacher = classMatching.getTeacher();
        String refuseReason = request.refuseReason();
        sendBizppurioMessage(teacher, refuseReason);
    }

    private void sendBizppurioMessage(Teacher teacher, String refuseReason) {
        if (refuseReason.equals(UNABLE_NOW.getReason())) {
            teacherUpdateService.plusRefuseCount(teacher);
            bizppurioEventPublisher.publishMatchingRefuseCaseNowEvent(mapToMatchingRefuseCaseNowEvent(teacher));
            return;
        }
        if (refuseReason.equals(UNABLE_DISTRICT.getReason())) {
            teacherUpdateService.plusRefuseCount(teacher);
            bizppurioEventPublisher.publishMatchingRefuseCaseDistrictEvent(mapToMatchingRefuseCaseDistrictEvent(teacher));
            return;
        }
        teacherUpdateService.clearRefuseCount(teacher);
        bizppurioEventPublisher.publishMatchingRefuseCaseEvent(mapToMatchingRefuseCaseEvent(teacher));
    }

    public void acceptClassMatching(String applicationFormId, long teacherId, String phoneNumber) {
        ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(applicationFormId, teacherId, phoneNumber);
        if (!classMatching.isWaiting())
            throw new MatchingStatusException(classMatching.getClassMatchingId());
        classMatchingUpdateService.updateAccept(classMatching);

        Teacher teacher = classMatching.getTeacher();
        teacherUpdateService.clearRefuseCount(teacher);
        bizppurioEventPublisher.publishMatchingAcceptCaseInfoEvent(mapToMatchingAcceptCaseEvent(classMatching));
    }
}
