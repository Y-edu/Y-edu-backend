package com.yedu.backend.domain.parents.application.usecase;

import com.yedu.backend.domain.matching.application.usecase.ClassMatchingManageUseCase;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.service.ParentsGetService;
import com.yedu.backend.domain.parents.domain.service.ParentsSaveService;
import com.yedu.backend.domain.parents.domain.service.ParentsUpdateService;
import com.yedu.backend.domain.teacher.application.usecase.TeacherInfoUseCase;
import com.yedu.backend.domain.teacher.application.usecase.TeacherManageUseCase;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.global.event.publisher.BizppurioEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yedu.backend.domain.parents.application.mapper.ParentsMapper.*;
import static com.yedu.backend.global.event.mapper.EventMapper.mapToNotifyCallingEvent;

@RequiredArgsConstructor
@Service
@Transactional
public class ParentsManageUseCase {
    private final ParentsGetService parentsGetService;
    private final ParentsSaveService parentsSaveService;
    private final ParentsUpdateService parentsUpdateService;
    private final TeacherManageUseCase teacherManageUseCase;
    private final TeacherInfoUseCase teacherInfoUseCase;
    private final ClassMatchingManageUseCase classMatchingManageUseCase;
    private final BizppurioEventPublisher bizppurioEventPublisher;

    public void saveParentsAndApplication(ApplicationFormRequest request) {
        Parents parents = parentsGetService.optionalParentsByPhoneNumber(request.phoneNumber())
                .orElseGet(() -> parentsSaveService.saveParents(mapToParents(request)));

        parentsUpdateService.updateCount(parents); //todo : 나중에 학부모 생성과 신청서 생성이 분리되면 나누어질 부분
        ApplicationForm applicationForm = mapToApplicationForm(parents, request);
        List<Goal> goals = request.classGoals().stream()
                .map(goal -> mapToGoal(applicationForm, goal))
                .toList();
        parentsSaveService.saveApplication(applicationForm, goals);
        List<Teacher> teachers = teacherInfoUseCase.allApplicationFormTeacher(applicationForm);
        List<ClassMatching> classMatchings = classMatchingManageUseCase.saveAllClassMatching(teachers, applicationForm);// 매칭 저장
        teacherManageUseCase.notifyClass(classMatchings); // 선생님한테 알림톡 전송
        bizppurioEventPublisher.publishNotifyCallingEvent(mapToNotifyCallingEvent(parents));
    }
}
