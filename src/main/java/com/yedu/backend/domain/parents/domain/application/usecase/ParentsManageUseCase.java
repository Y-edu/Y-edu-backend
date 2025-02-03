package com.yedu.backend.domain.parents.domain.application.usecase;

import com.yedu.backend.domain.parents.domain.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.service.ParentsSaveService;
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

    public void saveParentsAndApplication(ApplicationFormRequest request) {
        Parents parents = mapToParents(request);
        parents = parentsSaveService.saveParents(parents);
        ApplicationForm applicationForm = mapToApplicationForm(parents, request);
        List<Goal> goals = request.classGoals().stream()
                .map(goal -> mapToGoal(applicationForm, goal))
                .toList();
        parentsSaveService.saveApplication(applicationForm, goals);
    }
}
