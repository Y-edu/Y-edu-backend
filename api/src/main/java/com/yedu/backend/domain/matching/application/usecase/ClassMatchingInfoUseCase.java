package com.yedu.backend.domain.matching.application.usecase;

import static com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper.mapToApplicationFormToTeacherResponse;

import com.yedu.backend.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.service.ParentsGetService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassMatchingInfoUseCase {
  private final ClassMatchingGetService classMatchingGetService;
  private final ParentsGetService parentsGetService;

  public ClassMatchingForTeacherResponse applicationFormToTeacher(
      String applicationFormId, long teacherId, String phoneNumber) {
    ApplicationForm applicationForm = parentsGetService.applicationFormByFormId(applicationFormId);
    List<String> goals =
        parentsGetService.goalsByApplicationForm(applicationForm).stream()
            .map(Goal::getClassGoal)
            .toList();

    ClassMatching classMatching =
        classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(
            applicationFormId, teacherId, phoneNumber);

    return mapToApplicationFormToTeacherResponse(classMatching, applicationForm, goals);
  }
}
