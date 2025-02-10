package com.yedu.backend.domain.teacher.application.usecase;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.yedu.backend.domain.teacher.application.dto.res.*;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yedu.backend.domain.teacher.application.mapper.TeacherMapper.*;
import static com.yedu.backend.domain.teacher.domain.entity.QTeacherAvailable.teacherAvailable;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
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

    public MathCurriculumResponse curriculumMath(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        TeacherMath math = teacherGetService.mathByTeacher(teacher);
        return mapToMathCurriculumResponse(math);
    }

    public EnglishCurriculumResponse curriculumEnglish(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        TeacherEnglish english = teacherGetService.englishByTeacher(teacher);
        return mapToEnglishCurriculumResponse(teacher.getTeacherInfo(), english);
    }

    public DistrictAndTimeResponse districtAndTime(long teacherId) {
        Teacher teacher = teacherGetService.byId(teacherId);
        List<TeacherAvailable> teacherAvailables = teacherGetService.availablesByTeacher(teacher);
        List<TeacherDistrict> teacherDistricts = teacherGetService.districtsByTeacher(teacher);

        List<String> districts = teacherDistricts.stream()
                .map(teacherDistrict -> teacherDistrict.getDistrict().getDescription())
                .toList();

        SortedMap<Day, List<LocalTime>> availableTimes = new TreeMap<>(Comparator.comparingInt(Day::getDayNum));
        Arrays.stream(Day.values()).forEach(day -> availableTimes.put(day, new ArrayList<>()));

        teacherAvailables.forEach(teacherAvailable -> {
            Day day = teacherAvailable.getDay();
            List<LocalTime> availables = availableTimes.get(day);
            availables.add(teacherAvailable.getAvailableTime());
        });

        return mapToDistrictAndTimeResponse(districts, availableTimes);
    }
}
