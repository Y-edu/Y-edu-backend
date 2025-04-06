package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.admin.domain.service.ResponseRateStorage;
import com.yedu.backend.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingSaveService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingUpdateService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.backend.global.bizppurio.application.usecase.BizppurioTeacherMessage;
import com.yedu.backend.global.exception.matching.MatchingStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yedu.backend.domain.matching.application.constant.RefuseReason.UNABLE_DISTRICT;
import static com.yedu.backend.domain.matching.application.constant.RefuseReason.UNABLE_NOW;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassMatchingManageUseCase {
    private final ClassMatchingSaveService classMatchingSaveService;
    private final ClassMatchingGetService classMatchingGetService;
    private final ClassMatchingUpdateService classMatchingUpdateService;
    private final ResponseRateStorage responseRateStorage;
    private final TeacherUpdateService teacherUpdateService;
    private final BizppurioTeacherMessage bizppurioTeacherMessage;

    public void saveAllClassMatching(List<Teacher> teachers, ApplicationForm applicationForm) {
        teachers.stream()
                .map(teacher -> {
                    teacherUpdateService.plusRequestCount(teacher);
                    return ClassMatchingMapper.mapToClassMatching(teacher, applicationForm);
                })
                .toList()
                .forEach(classMatchingSaveService::save);
    }

    public void refuseClassMatching(String applicationFormId, long teacherId, String phoneNumber, ClassMatchingRefuseRequest request) {
        ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(applicationFormId, teacherId, phoneNumber);
        if (!classMatching.isWaiting())
            throw new MatchingStatusException(classMatching.getClassMatchingId());
        classMatchingUpdateService.updateRefuse(classMatching, request);
        Teacher teacher = classMatching.getTeacher();
        String refuseReason = request.refuseReason();
        sendBizppurioMessage(teacher, refuseReason);

        plusResponseCount(teacherId, teacher);
    }

    private void sendBizppurioMessage(Teacher teacher, String refuseReason) {
        if (refuseReason.equals(UNABLE_NOW.getReason())) {
            teacherUpdateService.plusRefuseCount(teacher);
            bizppurioTeacherMessage.refuseCaseNow(teacher);
            return;
        }
        if (refuseReason.equals(UNABLE_DISTRICT.getReason())) {
            teacherUpdateService.plusRefuseCount(teacher);
            bizppurioTeacherMessage.refuseCaseDistrict(teacher);
            return;
        }
        teacherUpdateService.clearRefuseCount(teacher);
        bizppurioTeacherMessage.refuseCase(teacher);
    }

    public void acceptClassMatching(String applicationFormId, long teacherId, String phoneNumber) {
        ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(applicationFormId, teacherId, phoneNumber);
        if (!classMatching.isWaiting())
            throw new MatchingStatusException(classMatching.getClassMatchingId());
        classMatchingUpdateService.updateAccept(classMatching);

        ApplicationForm applicationForm = classMatching.getApplicationForm();
        Teacher teacher = classMatching.getTeacher();
        teacherUpdateService.clearRefuseCount(teacher);
        bizppurioTeacherMessage.acceptCase(applicationForm, teacher);

        plusResponseCount(teacherId, teacher);
    }

    private void plusResponseCount(long teacherId, Teacher teacher) {
        if (!responseRateStorage.has(teacherId)){
            return;
        }
        teacherUpdateService.plusResponseCount(teacher);
    }
}
