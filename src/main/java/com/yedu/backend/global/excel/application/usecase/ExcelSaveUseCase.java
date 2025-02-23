package com.yedu.backend.global.excel.application.usecase;

import com.yedu.backend.domain.teacher.application.mapper.TeacherMapper;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.backend.domain.teacher.domain.service.TeacherSaveService;
import com.yedu.backend.global.config.s3.S3UploadService;
import com.yedu.backend.global.excel.application.dto.TeacherInfoRequest;
import com.yedu.backend.global.excel.application.dto.TeacherInfoRequest.*;
import com.yedu.backend.global.excel.application.dto.TeacherInfoRequest.Math;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yedu.backend.domain.teacher.application.mapper.TeacherMapper.mapToTeacherDistrict;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ExcelSaveUseCase {
    private final TeacherSaveService teacherSaveService;
    private final S3UploadService s3UploadService;

    public void saveTeacherByExcelFile(MultipartFile file) {
        try {
            readExcelFile(file).forEach(request -> {
                        log.info("request : {}", request);
                        Teacher teacher = TeacherMapper.mapToTeacher(request);
                        List<TeacherDistrict> districts = request.region()
                                .stream()
                                .map(region -> {
                                    region = region.trim();
                                    return mapToTeacherDistrict(teacher, region);
                                })
                                .toList();// 선생님 교육 가능 구역
                        TeacherEnglish english = request.englishPossible() ? TeacherMapper.mapToTeacherEnglish(teacher, request) : null;
                        TeacherMath math = request.mathPossible() ? TeacherMapper.mapToTeacherMath(teacher, request) : null;
                        teacherSaveService.saveTeacher(teacher, new ArrayList<>(), districts, english, math);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<TeacherInfoRequest> readExcelFile(MultipartFile file) throws Exception {
        List<TeacherInfoRequest> records = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
        boolean isHeader = true;

        for (Row row : sheet) {
            if (isHeader) { // 첫 번째 행(헤더) 건너뛰기
                isHeader = false;
                continue;
            }
            if (isRowEmpty(row))
                break;

            // 각 셀 값 읽기
            String email = getStringValue(row.getCell(1));
            String name = getStringValue(row.getCell(2));
            String nickName = getStringValue(row.getCell(3));
            String phoneNumber = getStringValue(row.getCell(4));
            double birthNum = getNumericValue(row.getCell(5));
            String birth = birthNum == 0 ? null : String.valueOf(birthNum);
            String gender = getStringValue(row.getCell(6));
            String university = getStringValue(row.getCell(7));
            String major = getStringValue(row.getCell(8));
            String highSchool = getStringValue(row.getCell(9));
            String highSchoolType = getStringValue(row.getCell(10));
            String introduce = getStringValue(row.getCell(11));
            String teachingStyle1 = getStringValue(row.getCell(12));
            String teachingStyleInfo1 = getStringValue(row.getCell(13));
            String teachingStyle2 = getStringValue(row.getCell(14));
            String teachingStyleInfo2 = getStringValue(row.getCell(15));

            boolean englishPossible = getBooleanValueByString(row.getCell(16));
            int englishTeachingHistory = (int) getNumericValue(row.getCell(17));
            String englishAppealPoint = getStringValue(row.getCell(18));
            String englishTeachingExperience = getStringValue(row.getCell(19));
            String englishForeignExperience = getStringValue(row.getCell(20));
            String englishTeachingStyle = getStringValue(row.getCell(21));
            String englishManagementStyle = getStringValue(row.getCell(22));

            boolean mathPossible = getBooleanValueByString(row.getCell(23));
            int mathTeachingHistory = (int) getNumericValue(row.getCell(24));
            String mathAppealPoint = getStringValue(row.getCell(25));
            String mathTeachingExperience = getStringValue(row.getCell(26));
            String mathTeachingStyle = getStringValue(row.getCell(27));
            String mathManageStyle = getStringValue(row.getCell(28));

            String recommendStudent = getStringValue(row.getCell(29));
            String comment = getStringValue(row.getCell(30));
            List<List<String>> available = new ArrayList<>();
            List<String> region = Arrays.stream((getStringValue(row.getCell(38)))
                            .split(",")).toList();
            String source = getStringValue(row.getCell(39));
            boolean marketingAgree = true;
            String profile = getStringValue(row.getCell(44));
//            String directDownloadUrl = extractFileId(profile);
//
//            byte[] imageBytes = downloadFileFromUrl(directDownloadUrl);
//            if (imageBytes == null) {
//                throw new RuntimeException("파일 다운로드 실패");
//            }
//
//            String profileUrl = s3UploadService.saveProfileByGoogleDrive(imageBytes);

            // 영어 과외 정보
            English english = new English(
                    englishAppealPoint,
                    englishTeachingExperience,
                    englishForeignExperience,
                    englishTeachingHistory,
                    englishTeachingStyle,
                    englishManagementStyle
            );

            // 수학 과외 정보
            Math math = new Math(
                    mathAppealPoint,
                    mathTeachingExperience,
                    mathTeachingHistory,
                    mathTeachingStyle,
                    mathManageStyle
            );

            // DTO 객체 생성 후 리스트에 추가
            records.add(new TeacherInfoRequest(
                    name, nickName, email, phoneNumber, birth, TeacherGender.valueOf(gender), university,
                    major, highSchool, highSchoolType, introduce, teachingStyle1, teachingStyleInfo1, teachingStyle2, teachingStyleInfo2,
                    englishPossible, mathPossible, recommendStudent, comment, available, region, source, marketingAgree, english, math
            ));
        }

        return records;
    }

    private String getStringValue(Cell cell) {
        return (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) ? cell.getStringCellValue() : null;
    }

    private double getNumericValue(Cell cell) {
        return (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) ? cell.getNumericCellValue() : 0;
    }

    private boolean getBooleanValueByString(Cell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) {
            String value = cell.getStringCellValue();
            return "네".equals(value);
        }
        return false;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) {
                return false;
            }
        }
        return true;
    }
//
//    private String extractFileId(String googleDriveUrl) {
//        if (googleDriveUrl.contains("id=")) {
//            return googleDriveUrl.split("id=")[1];
//        }
//        return googleDriveUrl.replaceAll("https://drive.google.com/file/d/", "")
//                .replaceAll("/view\\?usp=sharing", "");
//    }
//
//    private byte[] downloadFileFromUrl(String fileUrl) throws Exception {
//        URL url = new URL(fileUrl);
//        URLConnection connection = url.openConnection();
//        try (InputStream inputStream = connection.getInputStream()) {
//            return inputStream.readAllBytes();
//        }
//    }
}
