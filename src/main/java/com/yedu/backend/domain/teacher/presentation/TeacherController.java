package com.yedu.backend.domain.teacher.presentation;

import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.usecase.TeacherManageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherManageUseCase manageUseCase;

    @PostMapping("/form")
    public ResponseEntity saveByForm(@RequestBody TeacherInfoFormRequest request) {
        manageUseCase.saveTeacher(request);
        return ResponseEntity.ok().build();
    }
}
