package com.yedu.backend.domain.teacher.application.usecase;

import com.yedu.backend.domain.teacher.application.dto.res.*;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yedu.backend.domain.teacher.application.mapper.TeacherMapper.*;

@RequiredArgsConstructor
@Service
@Transactional
public class TeacherInfoUseCase {
    private final TeacherGetService teacherGetService;

    public TeacherCommonsInfoResponse teacherCommonsInfo(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        return mapToTeacherCommonsInfo(teacher);
    }

    public TeacherMathResponse teacherMathDetails(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        TeacherMath math = teacherGetService.mathByTeacher(teacher);
        return mapToTeacherMathResponse(teacher, math);
    }

    public TeacherEnglishResponse teacherEnglishDetails(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        TeacherEnglish english = teacherGetService.englishByTeacher(teacher);
        return mapToTeacherEnglish(teacher, english);
    }

    public CurriculumResponse curriculumMath(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        TeacherMath math = teacherGetService.mathByTeacher(teacher);
        return mapToCurriculumResponse(math);
    }

    public CurriculumResponse curriculumEnglish(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        TeacherEnglish english = teacherGetService.englishByTeacher(teacher);
        return mapToCurriculumResponse(english);
    }

    public DistrictAndTimeResponse districtAndTime(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        List<TeacherAvailable> teacherAvailables = teacherGetService.availablesByTeacher(teacher);
        List<TeacherDistrict> teacherDistricts = teacherGetService.districtsByTeacher(teacher);

        List<String> districts = teacherDistricts.stream()
                .map(teacherDistrict -> teacherDistrict.getDistrict().getDescription())
                .toList();

        Map<Day, List<LocalTime>> availableTimes = Arrays.stream(Day.values())
                .collect(Collectors.toMap(
                        day -> day,
                        day -> new ArrayList<>()
                ));

        teacherAvailables.forEach(teacherAvailable -> {
            Day day = teacherAvailable.getDay();
            List<LocalTime> availables = availableTimes.get(day);
            availables.add(teacherAvailable.getAvailableTime());
        });

        return mapToDistrictAndTimeResponse(districts, availableTimes);
    }
}
