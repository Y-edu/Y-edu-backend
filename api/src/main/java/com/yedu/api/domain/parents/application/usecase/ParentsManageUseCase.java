package com.yedu.api.domain.parents.application.usecase;

import static com.yedu.api.domain.parents.application.mapper.ParentsMapper.*;

import com.yedu.api.domain.matching.application.usecase.ClassMatchingManageUseCase;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.api.domain.parents.application.dto.req.ApplicationFormTimeTableRequest;
import com.yedu.api.domain.parents.application.dto.res.ApplicationFormTimeTableResponse;
import com.yedu.api.domain.parents.application.mapper.ApplicationFormAvailableMapper;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.api.domain.parents.domain.service.ApplicationFormAvailableCommandService;
import com.yedu.api.domain.parents.domain.service.ApplicationFormAvailableQueryService;
import com.yedu.api.domain.parents.domain.service.ParentsGetService;
import com.yedu.api.domain.parents.domain.service.ParentsSaveService;
import com.yedu.api.domain.parents.domain.service.ParentsUpdateService;
import com.yedu.api.domain.teacher.application.usecase.TeacherInfoUseCase;
import com.yedu.api.domain.teacher.application.usecase.TeacherManageUseCase;
import com.yedu.api.domain.teacher.domain.aggregate.TeacherWithAvailable;
import com.yedu.cache.support.RedisRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
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
  private final ApplicationFormRepository applicationFormRepository;

  public void saveParentsAndApplication(ApplicationFormRequest request) {
    Parents parents =
        parentsGetService
            .optionalParentsByPhoneNumber(request.phoneNumber())
            .orElseGet(() -> parentsSaveService.saveParents(mapToParents(request)));

    parentsUpdateService.updateCount(parents); // todo : 나중에 학부모 생성과 신청서 생성이 분리되면 나누어질 부분
    ApplicationForm applicationForm = mapToApplicationForm(parents, request);

    if (applicationFormRepository.findById(applicationForm.getApplicationFormId()).isPresent()) {
      log.warn("중복 제출- 과외 식별자 {}, {}", applicationForm.getApplicationFormId(), applicationForm);
      return;
    }

    if (applicationFormRepository.findByParentsAndDistrictAndWantedSubjectAndAgeAndClassCountAndCreatedAtAfter(applicationForm.getParents(), applicationForm.getDistrict(), applicationForm.getWantedSubject(),
        applicationForm.getAge(), applicationForm.getClassCount(), LocalDateTime.now().minusMinutes(1)).isPresent()) {
      log.warn("중복 제출- 과외 식별자 {}, {}", applicationForm.getApplicationFormId(), applicationForm);
      return;
    }

    List<Goal> goals =
        request.classGoals().stream().map(goal -> mapToGoal(applicationForm, goal)).toList();
    parentsSaveService.saveApplication(applicationForm, goals);

    List<ApplicationFormAvailable> applicationFormAvailables =
        ApplicationFormAvailableMapper.map(
            request.dayTimes(), applicationForm.getApplicationFormId());
    applicationFormAvailableCommandService.saveAll(applicationFormAvailables);

    TeacherWithAvailable teacherWithAvailable =
        teacherInfoUseCase.allApplicationFormTeacher(applicationForm);
    int classCount = getClassCount(applicationForm.getClassCount());
    List<ClassMatching> classMatchings =
        classMatchingManageUseCase.saveAllClassMatching(
            teacherWithAvailable.availableTeacher(classCount, applicationFormAvailables),
            applicationForm); // 매칭 저장
    teacherManageUseCase.notifyClass(
        classMatchings, applicationForm.getApplicationFormId()); // 선생님한테 알림톡 전송
  }

  public ApplicationFormTimeTableResponse retrieveTimeTable(
      ApplicationFormTimeTableRequest request) {
    if (StringUtils.hasText(request.applicationFormId())) {
      return Optional.of(applicationFormAvailableQueryService.query(request.applicationFormId()))
          .map(ApplicationFormAvailableMapper::map)
          .get();
    }

    return redisRepository
        .getValues(request.token()) // todo 토큰저장 & 조회로직 분리
        .map(applicationFormAvailableQueryService::query)
        .map(ApplicationFormAvailableMapper::map)
        .orElseGet(ApplicationFormTimeTableResponse::empty);
  }
}
