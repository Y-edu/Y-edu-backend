package com.yedu.backend.global.event.mapper;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.backend.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.common.event.discord.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DiscordEventMapper {
    public static ScheduleCancelEvent mapToScheduleCancelEvent(Teacher teacher, ApplicationForm applicationForm, String refuseReason) {
        String parentName = safeText(() -> applicationForm.getParents().getKakaoName());
        String teacherName = safeText(() -> teacher.getTeacherInfo().getName());
        return new ScheduleCancelEvent(parentName, teacherName, refuseReason);
    }

    private static String safeText(Supplier<String> supplier) {
        return Optional.ofNullable(supplier.get())
                .filter(StringUtils::hasText)
                .orElse("(이름 미입력)");
    }

    public static TeacherRegisterEvent mapToTeacherRegisterEvent(Teacher teacher, List<TeacherDistrict> districts) {
        StringBuilder subject = new StringBuilder();
        StringBuilder teacherLink = new StringBuilder();
        if (teacher.getTeacherClassInfo().isMathPossible()) {
            subject.append("수학 ");
            teacherLink.append("✅ 수학 : ")
                    .append("https://yedu-tutor.com/teacher/")
                    .append(teacher.getTeacherId()).append("?subject=math\n");
        }
        if (teacher.getTeacherClassInfo().isEnglishPossible()) {
            subject.append("영어");
            teacherLink.append("✅ 영어 : ")
                    .append("https://yedu-tutor.com/teacher/")
                    .append(teacher.getTeacherId()).append("?subject=english\n");
        }
        StringBuilder regions = new StringBuilder();
        districts.forEach(district -> regions.append(district.getDistrict()).append("\n"));
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        return new TeacherRegisterEvent(teacherInfo.getName(), teacherInfo.getNickName(), subject.toString(), teacherLink.toString(), regions.toString());
    }

    public static AlarmTalkErrorInfoEvent mapToAlarmTalkErrorInfoEvent(String phoneNumber, String content, String code, String message) {
        return new AlarmTalkErrorInfoEvent(phoneNumber, content, code, message);
    }
}
