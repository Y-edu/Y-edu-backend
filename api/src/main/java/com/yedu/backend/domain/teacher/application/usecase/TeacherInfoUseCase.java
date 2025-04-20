package com.yedu.backend.domain.teacher.application.usecase;

import static com.yedu.backend.domain.teacher.application.mapper.TeacherMapper.*;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.application.dto.res.DistrictAndTimeResponse;
import com.yedu.backend.domain.teacher.application.dto.res.EnglishCurriculumResponse;
import com.yedu.backend.domain.teacher.application.dto.res.MathCurriculumResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherCommonsInfoResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherEnglishResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherInfoResponse;
import com.yedu.backend.domain.teacher.application.dto.res.TeacherMathResponse;
import com.yedu.backend.domain.teacher.domain.aggregate.TeacherWithAvailable;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.backend.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.backend.domain.teacher.domain.entity.TeacherMath;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import java.time.LocalTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    List<String> districts = getDistricts(teacher);
    SortedMap<Day, List<LocalTime>> availableTimes = getDayListSortedMap(teacher);

    return mapToDistrictAndTimeResponse(districts, availableTimes);
  }

  public TeacherInfoResponse teacherMyPage(String name, String phoneNumber) {
    Teacher teacher = teacherGetService.byNameAndPhoneNumber(name, phoneNumber);
    List<String> districts = getDistricts(teacher);
    SortedMap<Day, List<LocalTime>> available = getDayListSortedMap(teacher);
    return mapToTeacherInfoResponse(teacher, districts, available);
  }

  private SortedMap<Day, List<LocalTime>> getDayListSortedMap(Teacher teacher) {
    List<TeacherAvailable> teacherAvailables = teacherGetService.availablesByTeacher(teacher);

    SortedMap<Day, List<LocalTime>> availableTimes =
        new TreeMap<>(Comparator.comparingInt(Day::getDayNum));
    Arrays.stream(Day.values()).forEach(day -> availableTimes.put(day, new ArrayList<>()));

    teacherAvailables.forEach(
        teacherAvailable -> {
          Day day = teacherAvailable.getDay();
          List<LocalTime> availables = availableTimes.get(day);
          availables.add(teacherAvailable.getAvailableTime());
        });
    return availableTimes;
  }

  private List<String> getDistricts(Teacher teacher) {
    List<TeacherDistrict> teacherDistricts = teacherGetService.districtsByTeacher(teacher);

    return teacherDistricts.stream()
        .map(teacherDistrict -> teacherDistrict.getDistrict().getDescription())
        .toList();
  }

  public TeacherWithAvailable allApplicationFormTeacher(ApplicationForm applicationForm) {
    return teacherGetService.applicationFormTeachers(applicationForm);
  }
}
