package com.yedu.backend.global.excel.presentation;

import com.yedu.backend.global.excel.application.usecase.ExcelManageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelManageUseCase manageUseCase;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadExcelFile(@RequestPart MultipartFile file) {
        manageUseCase.saveTeacherByExcelFile(file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> uploadProfile(@RequestPart List<MultipartFile> profiles) {
        manageUseCase.saveTeacherProfile(profiles);
        return ResponseEntity.ok().build();
    }
}
