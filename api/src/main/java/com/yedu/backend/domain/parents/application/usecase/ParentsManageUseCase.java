package com.yedu.backend.domain.parents.application.usecase;

import com.yedu.backend.domain.matching.application.usecase.ClassMatchingManageUseCase;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.application.dto.req.ApplicationFormTimeTableRequest;
import com.yedu.backend.domain.parents.application.dto.res.ApplicationFormTimeTableResponse;
import com.yedu.backend.domain.parents.application.mapper.ApplicationFormAvailableMapper;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.service.ApplicationFormAvailableCommandService;
import com.yedu.backend.domain.parents.domain.service.ApplicationFormAvailableQueryService;
import com.yedu.backend.domain.parents.domain.service.ParentsGetService;
import com.yedu.backend.domain.parents.domain.service.ParentsSaveService;
import com.yedu.backend.domain.parents.domain.service.ParentsUpdateService;
import com.yedu.backend.domain.teacher.application.usecase.TeacherInfoUseCase;
import com.yedu.backend.domain.teacher.application.usecase.TeacherManageUseCase;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.common.redis.RedisRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.util.StringUtils;

import static com.yedu.backend.domain.parents.application.mapper.ParentsMapper.*;

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
    private final ApplicationFormAvailableQueryService applicationFormAvailableQueryService;
    private final ApplicationFormAvailableCommandService applicationFormAvailableCommandService;
    private final RedisRepository redisRepository;

    public void saveParentsAndApplication(ApplicationFormRequest request) {
        Parents parents = parentsGetService.optionalParentsByPhoneNumber(request.phoneNumber())
                .orElseGet(() -> parentsSaveService.saveParents(mapToParents(request)));

        parentsUpdateService.updateCount(parents); //todo : 나중에 학부모 생성과 신청서 생성이 분리되면 나누어질 부분
        ApplicationForm applicationForm = mapToApplicationForm(parents, request);
        List<Goal> goals = request.classGoals().stream()
                .map(goal -> mapToGoal(applicationForm, goal))
                .toList();
        parentsSaveService.saveApplication(applicationForm, goals);

        List<ApplicationFormAvailable> applicationFormAvailables = ApplicationFormAvailableMapper.map(request.dayTimes(), applicationForm.getApplicationFormId());
        applicationFormAvailableCommandService.saveAll(applicationFormAvailables);

        List<Teacher> teachers = teacherInfoUseCase.allApplicationFormTeacher(applicationForm);
        List<ClassMatching> classMatchings = classMatchingManageUseCase.saveAllClassMatching(teachers, applicationForm);// 매칭 저장
        teacherManageUseCase.notifyClass(classMatchings); // 선생님한테 알림톡 전송
    }

    public ApplicationFormTimeTableResponse retrieveTimeTable(ApplicationFormTimeTableRequest request) {
        if (StringUtils.hasText(request.applicationFormId())){
            return Optional.of(applicationFormAvailableQueryService.query(request.applicationFormId()))
                .map(ApplicationFormAvailableMapper::map)
                .get();
        }

        return redisRepository.getValues(request.token())
            .map(applicationFormAvailableQueryService::query)
            .map(ApplicationFormAvailableMapper::map)
            .orElseGet(ApplicationFormTimeTableResponse::empty);

    }
}
