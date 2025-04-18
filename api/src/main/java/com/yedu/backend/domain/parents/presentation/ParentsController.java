package com.yedu.backend.domain.parents.presentation;

import com.yedu.backend.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.application.dto.req.ApplicationFormTimeTableRequest;
import com.yedu.backend.domain.parents.application.dto.res.ApplicationFormTimeTableResponse;
import com.yedu.backend.domain.parents.application.usecase.ParentsManageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/parents")
@RestController
@RequiredArgsConstructor
@Tag(name = "PARENTS Controller")
public class ParentsController {
    private final ParentsManageUseCase parentsManageUseCase;

    @PostMapping("/save/application")
    @Operation(summary = "Tally 제출 - 학부모 신청건 API")
    public ResponseEntity saveApplication(@RequestBody ApplicationFormRequest request) {
        parentsManageUseCase.saveParentsAndApplication(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/timetable")
    @Operation(summary = "과외의 타임테이블 조회")
    public ResponseEntity<ApplicationFormTimeTableResponse> retrieveTimeTable(@Valid @ParameterObject ApplicationFormTimeTableRequest request){
        ApplicationFormTimeTableResponse response = parentsManageUseCase.retrieveTimeTable(request);

        return ResponseEntity.ok().body(response);
    }
}
