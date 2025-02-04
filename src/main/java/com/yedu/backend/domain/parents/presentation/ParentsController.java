package com.yedu.backend.domain.parents.presentation;

import com.yedu.backend.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.application.usecase.ParentsManageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/parents")
@RestController
@RequiredArgsConstructor
public class ParentsController {
    private final ParentsManageUseCase parentsManageUseCase;

    @PostMapping("/save/application")
    public ResponseEntity saveApplication(@RequestBody ApplicationFormRequest request) {
        parentsManageUseCase.saveParentsAndApplication(request);
        return ResponseEntity.ok().build();
    }
}
