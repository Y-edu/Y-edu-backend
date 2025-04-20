package com.yedu.backend.global.excel.application.mapper;

import static com.yedu.backend.global.excel.constant.TeacherExcelColumn.*;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.backend.global.excel.application.dto.TeacherInfoRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ExcelMapper {

  public static TeacherInfoRequest mapToInfoRequest(Row row) {
    String email = getStringValue(row.getCell(EMAIL.getIndex()));
    String name = getStringValue(row.getCell(NAME.getIndex()));
    String nickName = getStringValue(row.getCell(NICKNAME.getIndex()));
    String phoneNumber = getStringValue(row.getCell(PHONE_NUMBER.getIndex()));
    double birthNum = getNumericValue(row.getCell(BIRTH.getIndex()));
    String birth = birthNum == 0 ? null : String.valueOf(birthNum);
    String gender = getStringValue(row.getCell(GENDER.getIndex()));
    String university = getStringValue(row.getCell(UNIVERSITY.getIndex()));
    String major = getStringValue(row.getCell(MAJOR.getIndex()));
    String highSchool = getStringValue(row.getCell(HIGH_SCHOOL.getIndex()));
    String highSchoolType = getStringValue(row.getCell(HIGH_SCHOOL_TYPE.getIndex()));
    String introduce = getStringValue(row.getCell(INTRODUCE.getIndex()));
    String teachingStyle1 = getStringValue(row.getCell(TEACHING_STYLE_1.getIndex()));
    String teachingStyleInfo1 = getStringValue(row.getCell(TEACHING_STYLE_INFO_1.getIndex()));
    String teachingStyle2 = getStringValue(row.getCell(TEACHING_STYLE_2.getIndex()));
    String teachingStyleInfo2 = getStringValue(row.getCell(TEACHING_STYLE_INFO_2.getIndex()));
    boolean englishPossible = getBooleanValueByString(row.getCell(ENGLISH_POSSIBLE.getIndex()));
    boolean mathPossible = getBooleanValueByString(row.getCell(MATH_POSSIBLE.getIndex()));
    String recommendStudent = getStringValue(row.getCell(RECOMMEND_STUDENT.getIndex()));
    String comment = getStringValue(row.getCell(COMMENT.getIndex()));
    List<List<String>> available = new ArrayList<>();
    List<String> region =
        Arrays.stream((getStringValue(row.getCell(REGION.getIndex()))).split(",")).toList();
    String source = getStringValue(row.getCell(SOURCE.getIndex()));
    boolean marketingAgree = true;

    return new TeacherInfoRequest(
        name,
        nickName,
        email,
        phoneNumber,
        birth,
        TeacherGender.valueOf(gender),
        university,
        major,
        highSchool,
        highSchoolType,
        introduce,
        teachingStyle1,
        teachingStyleInfo1,
        teachingStyle2,
        teachingStyleInfo2,
        englishPossible,
        mathPossible,
        recommendStudent,
        comment,
        available,
        region,
        source,
        marketingAgree,
        getEnglish(row),
        getMath(row));
  }

  private static TeacherInfoRequest.Math getMath(Row row) {
    int mathTeachingHistory = (int) getNumericValue(row.getCell(MATH_TEACHING_HISTORY.getIndex()));
    String mathAppealPoint = getStringValue(row.getCell(MATH_APPEAL_POINT.getIndex()));
    String mathTeachingExperience =
        getStringValue(row.getCell(MATH_TEACHING_EXPERIENCE.getIndex()));
    String mathTeachingStyle = getStringValue(row.getCell(MATH_TEACHING_STYLE.getIndex()));
    String mathManageStyle = getStringValue(row.getCell(MATH_MANAGE_STYLE.getIndex()));
    return new TeacherInfoRequest.Math(
        mathAppealPoint,
        mathTeachingExperience,
        mathTeachingHistory,
        mathTeachingStyle,
        mathManageStyle);
  }

  private static TeacherInfoRequest.English getEnglish(Row row) {
    int englishTeachingHistory =
        (int) getNumericValue(row.getCell(ENGLISH_TEACHING_HISTORY.getIndex()));
    String englishAppealPoint = getStringValue(row.getCell(ENGLISH_APPEAL_POINT.getIndex()));
    String englishTeachingExperience =
        getStringValue(row.getCell(ENGLISH_TEACHING_EXPERIENCE.getIndex()));
    String englishForeignExperience =
        getStringValue(row.getCell(ENGLISH_FOREIGN_EXPERIENCE.getIndex()));
    String englishTeachingStyle = getStringValue(row.getCell(ENGLISH_TEACHING_STYLE.getIndex()));
    String englishManagementStyle =
        getStringValue(row.getCell(ENGLISH_MANAGEMENT_STYLE.getIndex()));
    // 영어 과외 정보
    return new TeacherInfoRequest.English(
        englishAppealPoint,
        englishTeachingExperience,
        englishForeignExperience,
        englishTeachingHistory,
        englishTeachingStyle,
        englishManagementStyle);
  }

  private static String getStringValue(Cell cell) {
    return (cell != null
            && cell.getCellType() != CellType.BLANK
            && cell.getCellType() != CellType._NONE)
        ? cell.getStringCellValue()
        : null;
  }

  private static double getNumericValue(Cell cell) {
    return (cell != null
            && cell.getCellType() != CellType.BLANK
            && cell.getCellType() != CellType._NONE)
        ? cell.getNumericCellValue()
        : 0;
  }

  private static boolean getBooleanValueByString(Cell cell) {
    if (cell != null
        && cell.getCellType() != CellType.BLANK
        && cell.getCellType() != CellType._NONE) {
      String value = cell.getStringCellValue();
      return "네".equals(value);
    }
    return false;
  }
}
