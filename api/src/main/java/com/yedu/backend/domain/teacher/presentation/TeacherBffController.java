package com.yedu.backend.domain.teacher.presentation;

import com.yedu.backend.domain.teacher.application.dto.res.TeacherAllInformationResponse;
import com.yedu.backend.domain.teacher.application.usecase.TeacherBffInfoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bff/teacher")
@RequiredArgsConstructor
@Tag(name = "TEACHER Controller")
// TODO BFF server 로 로직 이관 필요
public class TeacherBffController {

    private final TeacherBffInfoUseCase bffInfoUseCase;

    @GetMapping("/info")
    @Operation(summary = "토큰으로 선생님 모든 정보 조회")
    public ResponseEntity<TeacherAllInformationResponse> retrieveAllInformation(String token) {
        TeacherAllInformationResponse response = bffInfoUseCase.retrieveAllInformation(token);

        return ResponseEntity.ok(response);
    }
}
