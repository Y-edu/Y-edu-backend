package com.yedu.backend.domain.parents.domain.application.usecase;

import com.yedu.backend.domain.parents.domain.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.service.ParentsSaveService;
import com.yedu.backend.domain.parents.domain.service.ParentsUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yedu.backend.domain.parents.domain.application.mapper.ParentsMapper.*;

@RequiredArgsConstructor
@Service
@Transactional
public class ParentsManageUseCase {
    private final ParentsSaveService parentsSaveService;
    private final ParentsUpdateService parentsUpdateService;

    public void saveParentsAndApplication(ApplicationFormRequest request) {
        Parents parents = mapToParents(request);
        parents = parentsSaveService.saveParents(parents);

        parentsUpdateService.updateCount(parents); //todo : 나중에 학부모 생성과 신청서 생성이 분리되면 나누어질 부분
        ApplicationForm applicationForm = mapToApplicationForm(parents, request);
        List<Goal> goals = request.classGoals().stream()
                .map(goal -> mapToGoal(applicationForm, goal))
                .toList();
        parentsSaveService.saveApplication(applicationForm, goals);
    }
}
