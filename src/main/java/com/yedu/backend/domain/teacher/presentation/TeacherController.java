package com.yedu.backend.domain.teacher.presentation;

import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherProfileFormRequest;
import com.yedu.backend.domain.teacher.application.usecase.TeacherManageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/form/profile")
    public ResponseEntity saveProfileByForm(@RequestPart TeacherProfileFormRequest request, @RequestPart MultipartFile profile) {
        manageUseCase.saveTeacherProfile(profile, request);
        return ResponseEntity.ok().build();
    }
}
