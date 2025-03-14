package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingSaveService;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingUpdateService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.global.bizppurio.application.usecase.BizppurioTeacherMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yedu.backend.domain.matching.domain.entity.constant.RefuseReason.UNABLE_DISTRICT;
import static com.yedu.backend.domain.matching.domain.entity.constant.RefuseReason.UNABLE_NOW;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassMatchingManageUseCase {
    private final ClassMatchingSaveService classMatchingSaveService;
    private final ClassMatchingGetService classMatchingGetService;
    private final ClassMatchingUpdateService classMatchingUpdateService;
    private final BizppurioTeacherMessage bizppurioTeacherMessage;

    public void saveAllClassMatching(List<Teacher> teachers, ApplicationForm applicationForm) {
        teachers.stream()
                .map(teacher -> ClassMatchingMapper.mapToClassMatching(teacher, applicationForm))
                .toList()
                .forEach(classMatchingSaveService::save);
    }

    public void refuseClassMatching(String applicationFormId, long teacherId, String phoneNumber, ClassMatchingRefuseRequest request) {
        ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(applicationFormId, teacherId, phoneNumber);
        if (!classMatching.isWaiting())
            throw new IllegalArgumentException();
        classMatchingUpdateService.updateRefuse(classMatching, request);
        Teacher teacher = classMatching.getTeacher();
        String refuseReason = request.refuseReason();
        if (refuseReason.equals(UNABLE_NOW.getReason())) {
            bizppurioTeacherMessage.refuseCaseNow(teacher);
            return;
        }
        if (refuseReason.equals(UNABLE_DISTRICT.getReason())) {
            bizppurioTeacherMessage.refuseCaseDistrict(teacher);
            return;
        }
        bizppurioTeacherMessage.refuseCase(teacher);
    }

    public void acceptClassMatching(String applicationFormId, long teacherId, String phoneNumber) {
        ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(applicationFormId, teacherId, phoneNumber);
        if (!classMatching.isWaiting())
            throw new IllegalArgumentException();
        classMatchingUpdateService.updateAccept(classMatching);

        ApplicationForm applicationForm = classMatching.getApplicationForm();
        Teacher teacher = classMatching.getTeacher();
        bizppurioTeacherMessage.acceptCase(applicationForm, teacher);
    }
}
