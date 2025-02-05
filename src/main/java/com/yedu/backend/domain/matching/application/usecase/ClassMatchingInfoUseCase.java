package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.res.ClassMatchingForTeacherResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassMatchingGetService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.service.ParentsGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper.mapToApplicationFormToTeacherResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassMatchingInfoUseCase {
    private final ClassMatchingGetService classMatchingGetService;
    private final ParentsGetService parentsGetService;

    public ClassMatchingForTeacherResponse applicationFormToTeacher(String applicationFormId, long teacherId, String phoneNumber) {
        ApplicationForm applicationForm = parentsGetService.applicationFormByFormId(applicationFormId);
        List<String> goals = parentsGetService.goalsByApplicationForm(applicationForm)
                .stream()
                .map(Goal::getClassGoal)
                .toList();

        ClassMatching classMatching = classMatchingGetService.classMatchingByApplicationFormIdAndTeacherId(applicationFormId, teacherId, phoneNumber);
        if (!classMatching.isWaiting())
            throw new IllegalArgumentException();
        // todo : 알림톡을 통해 선생님들에게 보내는데, 거절할 때 선생님 특정을 어떻게 하지? 프론트 URL에 선생님 PK값을 포함하여 만들고, 신청하기 혹은 거절하기 누를 때 그걸 포함해서 요청해달라고 하기

        return mapToApplicationFormToTeacherResponse(applicationForm, goals);
    }
}
