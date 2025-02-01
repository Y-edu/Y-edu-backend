package com.yedu.backend.domain.teacher.presentation;

import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherProfileFormRequest;
import com.yedu.backend.domain.teacher.application.dto.res.*;
import com.yedu.backend.domain.teacher.application.usecase.TeacherInfoUseCase;
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
    private final TeacherInfoUseCase infoUseCase;

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

    @GetMapping("/details/info/{teacherId}")
    public ResponseEntity<TeacherCommonsInfoResponse> commonsInfo(@PathVariable long teacherId) {
        TeacherCommonsInfoResponse teacherCommonsInfoResponse = infoUseCase.teacherCommonsInfo(teacherId);
        return ResponseEntity.ok(teacherCommonsInfoResponse);
    }

    @GetMapping("/details/math/teacher/{teacherId}")
    public ResponseEntity<TeacherMathResponse> mathTeacherInfo(@PathVariable long teacherId) {
        TeacherMathResponse teacherMathResponse = infoUseCase.teacherMathDetails(teacherId);
        return ResponseEntity.ok(teacherMathResponse);
    }

    @GetMapping("/details/english/teacher/{teacherId}")
    public ResponseEntity<TeacherEnglishResponse> englishTeacherInfo(@PathVariable long teacherId) {
        TeacherEnglishResponse teacherEnglishResponse = infoUseCase.teacherEnglishDetails(teacherId);
        return ResponseEntity.ok(teacherEnglishResponse);
    }

    @GetMapping("/details/math/class/{teacherId}")
    public ResponseEntity<CurriculumResponse> mathCurriculumInfo(@PathVariable long teacherId) {
        CurriculumResponse curriculumResponse = infoUseCase.curriculumMath(teacherId);
        return ResponseEntity.ok(curriculumResponse);
    }

    @GetMapping("/details/english/class/{teacherId}")
    public ResponseEntity<CurriculumResponse> englishCurriculumInfo(@PathVariable long teacherId) {
        CurriculumResponse curriculumResponse = infoUseCase.curriculumEnglish(teacherId);
        return ResponseEntity.ok(curriculumResponse);
    }

    @GetMapping("/details/available/{teacherId}")
    public ResponseEntity<DistrictAndTimeResponse> availableInfo(@PathVariable long teacherId) {
        DistrictAndTimeResponse districtAndTimeResponse = infoUseCase.districtAndTime(teacherId);
        return ResponseEntity.ok(districtAndTimeResponse);
    }
}
