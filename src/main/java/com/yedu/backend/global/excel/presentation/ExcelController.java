package com.yedu.backend.global.excel.presentation;

import com.yedu.backend.global.excel.application.usecase.ExcelSaveUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelSaveUseCase saveUseCase;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadExcelFile(@RequestPart MultipartFile file) {
        saveUseCase.saveTeacherByExcelFile(file);
        return ResponseEntity.ok().build();
    }
}
