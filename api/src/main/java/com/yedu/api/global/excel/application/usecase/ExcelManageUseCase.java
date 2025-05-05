package com.yedu.api.global.excel.application.usecase;

import static com.yedu.api.domain.teacher.application.mapper.TeacherMapper.mapToTeacherDistrict;

import com.yedu.api.domain.teacher.application.mapper.TeacherMapper;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.api.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.api.domain.teacher.domain.entity.TeacherMath;
import com.yedu.api.domain.teacher.domain.service.TeacherGetService;
import com.yedu.api.domain.teacher.domain.service.TeacherSaveService;
import com.yedu.api.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.api.global.config.s3.S3UploadService;
import com.yedu.api.global.excel.application.dto.TeacherInfoRequest;
import com.yedu.api.global.excel.application.mapper.ExcelMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ExcelManageUseCase {
  private final TeacherSaveService teacherSaveService;
  private final TeacherGetService teacherGetService;
  private final TeacherUpdateService teacherUpdateService;
  private final ExcelParserUseCase excelParserUseCase;
  private final S3UploadService s3UploadService;

  public void saveTeacherByExcelFile(MultipartFile file) {
    try {
      List<TeacherInfoRequest> teacherInfoRequests =
          excelParserUseCase.parseExcel(file).stream().map(ExcelMapper::mapToInfoRequest).toList();
      teacherInfoRequests.forEach(
          request -> {
            log.info("request : {}", request);
            Teacher teacher = TeacherMapper.mapToTeacher(request);
            List<TeacherDistrict> districts =
                request.region().stream()
                    .map(
                        region -> {
                          region = region.trim();
                          return mapToTeacherDistrict(teacher, region);
                        })
                    .toList(); // 선생님 교육 가능 구역
            TeacherEnglish english =
                request.englishPossible()
                    ? TeacherMapper.mapToTeacherEnglish(teacher, request)
                    : null;
            TeacherMath math =
                request.mathPossible() ? TeacherMapper.mapToTeacherMath(teacher, request) : null;
            teacherSaveService.saveTeacher(teacher, new ArrayList<>(), districts, english, math);
          });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void saveTeacherProfile(List<MultipartFile> profiles) {
    profiles.stream()
        .forEach(
            profile -> {
              String fileName = profile.getOriginalFilename().replace(" ", "").split("-")[0];
              String[] split = fileName.split("_");
              String name = split[0];
              String nickName = split[1];
              log.info("name : {} nickName : {}", name, nickName);
              Teacher teacher = teacherGetService.byNameAndNickName(name, nickName);
              String profileUrl = s3UploadService.saveProfileFile(profile);
              teacherUpdateService.updateProfile(teacher, profileUrl);
            });
  }
}
