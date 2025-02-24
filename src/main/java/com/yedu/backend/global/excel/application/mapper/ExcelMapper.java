package com.yedu.backend.global.excel.application.mapper;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.backend.global.excel.application.dto.TeacherInfoRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelMapper {

    public static TeacherInfoRequest mapToInfoRequest(Row row) {
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
        boolean mathPossible = getBooleanValueByString(row.getCell(23));
        String recommendStudent = getStringValue(row.getCell(29));
        String comment = getStringValue(row.getCell(30));
        List<List<String>> available = new ArrayList<>();
        List<String> region = Arrays.stream((getStringValue(row.getCell(38)))
                .split(",")).toList();
        String source = getStringValue(row.getCell(39));
        boolean marketingAgree = true;

        return new TeacherInfoRequest(
                name, nickName, email, phoneNumber, birth, TeacherGender.valueOf(gender), university,
                major, highSchool, highSchoolType, introduce, teachingStyle1, teachingStyleInfo1, teachingStyle2, teachingStyleInfo2,
                englishPossible, mathPossible, recommendStudent, comment, available, region, source, marketingAgree, getEnglish(row), getMath(row)
        );
    }

    private static TeacherInfoRequest.Math getMath(Row row) {
        int mathTeachingHistory = (int) getNumericValue(row.getCell(24));
        String mathAppealPoint = getStringValue(row.getCell(25));
        String mathTeachingExperience = getStringValue(row.getCell(26));
        String mathTeachingStyle = getStringValue(row.getCell(27));
        String mathManageStyle = getStringValue(row.getCell(28));
        return new TeacherInfoRequest.Math(
                mathAppealPoint,
                mathTeachingExperience,
                mathTeachingHistory,
                mathTeachingStyle,
                mathManageStyle
        );
    }

    private static TeacherInfoRequest.English getEnglish(Row row) {
        int englishTeachingHistory = (int) getNumericValue(row.getCell(17));
        String englishAppealPoint = getStringValue(row.getCell(18));
        String englishTeachingExperience = getStringValue(row.getCell(19));
        String englishForeignExperience = getStringValue(row.getCell(20));
        String englishTeachingStyle = getStringValue(row.getCell(21));
        String englishManagementStyle = getStringValue(row.getCell(22));
        // 영어 과외 정보
        return new TeacherInfoRequest.English(
                englishAppealPoint,
                englishTeachingExperience,
                englishForeignExperience,
                englishTeachingHistory,
                englishTeachingStyle,
                englishManagementStyle
        );
    }

    private static String getStringValue(Cell cell) {
        return (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) ? cell.getStringCellValue() : null;
    }

    private static double getNumericValue(Cell cell) {
        return (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) ? cell.getNumericCellValue() : 0;
    }

    private static boolean getBooleanValueByString(Cell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) {
            String value = cell.getStringCellValue();
            return "네".equals(value);
        }
        return false;
    }
}
