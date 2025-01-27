package com.yedu.backend.domain.teacher.application.usecase;

import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.service.TeacherSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.yedu.backend.domain.teacher.application.mapper.TeacherMapper.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherManageUseCase {
    private final TeacherSaveService teacherSaveService;

    public void saveTeacher(TeacherInfoFormRequest request) {
        Teacher teacher = mapToTeacher(request); // 기본 선생님 정보
        List<TeacherDistrict> teacherDistricts = getTeacherDistricts(request, teacher);
        List<TeacherAvailable> teacherAvailables = getTeacherAvailables(request, teacher);
        TeacherEnglish english = getTeacherEnglish(request, teacher);
        TeacherMath math = getTeacherMath(request, teacher);
        teacherSaveService.saveTeacher(teacher, teacherAvailables, teacherDistricts, english, math);
    }

    private TeacherMath getTeacherMath(TeacherInfoFormRequest request, Teacher teacher) {
        TeacherMath math = null;
        if (request.mathPossible())
            math = mapToTeacherMath(teacher, request);
        return math;
    }

    private TeacherEnglish getTeacherEnglish(TeacherInfoFormRequest request, Teacher teacher) {
        TeacherEnglish english = null;
        if (request.englishPossible())
            english = mapToTeacherEnglish(teacher, request);
        return english;
    }

    private List<TeacherAvailable> getTeacherAvailables(TeacherInfoFormRequest request, Teacher teacher) {
        List<List<String>> availables = request.available();
        List<TeacherAvailable> teacherAvailables = new ArrayList<>();

        for (int day = 0; day < availables.size(); day++) {
            int finalDay = day;
            List<String> times = availables.get(day);
            times.stream()
                    .filter(time -> !time.equals("불가"))  // "불가"가 아닌 값만 필터링
                    .forEach(time -> {
                        TeacherAvailable teacherAvailable = mapToTeacherAvailable(teacher, finalDay, time);
                        teacherAvailables.add(teacherAvailable);
                    });
        }
        // 선생님 가능 시간 : teacherAvailables 생성
        return teacherAvailables;
    }

    private List<TeacherDistrict> getTeacherDistricts(TeacherInfoFormRequest request, Teacher teacher) {
        return request.region()
                .stream()
                .map(region -> mapToTeacherDistrict(teacher, region))
                .toList(); // 선생님 교육 가능 구역
    }
}
