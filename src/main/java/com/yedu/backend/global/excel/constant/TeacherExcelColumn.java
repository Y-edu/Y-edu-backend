package com.yedu.backend.global.excel.constant;

public enum TeacherExcelColumn {
    EMAIL(1),
    NAME(2),
    NICKNAME(3),
    PHONE_NUMBER(4),
    BIRTH(5),
    GENDER(6),
    UNIVERSITY(7),
    MAJOR(8),
    HIGH_SCHOOL(9),
    HIGH_SCHOOL_TYPE(10),
    INTRODUCE(11),
    TEACHING_STYLE_1(12),
    TEACHING_STYLE_INFO_1(13),
    TEACHING_STYLE_2(14),
    TEACHING_STYLE_INFO_2(15),
    ENGLISH_POSSIBLE(16),
    ENGLISH_TEACHING_HISTORY(17),
    ENGLISH_APPEAL_POINT(18),
    ENGLISH_TEACHING_EXPERIENCE(19),
    ENGLISH_FOREIGN_EXPERIENCE(20),
    ENGLISH_TEACHING_STYLE(21),
    ENGLISH_MANAGEMENT_STYLE(22),
    MATH_POSSIBLE(23),
    MATH_TEACHING_HISTORY(24),
    MATH_APPEAL_POINT(25),
    MATH_TEACHING_EXPERIENCE(26),
    MATH_TEACHING_STYLE(27),
    MATH_MANAGE_STYLE(28),
    RECOMMEND_STUDENT(29),
    COMMENT(30),
    REGION(38),
    SOURCE(39);

    private final int index;

    TeacherExcelColumn(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
