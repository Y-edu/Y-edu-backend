package com.yedu.backend.domain.teacher.domain.service;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.aggregate.TeacherWithAvailable;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.backend.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.backend.domain.teacher.domain.entity.TeacherMath;
import com.yedu.backend.domain.teacher.domain.repository.TeacherAvailableRepository;
import com.yedu.backend.domain.teacher.domain.repository.TeacherDistrictRepository;
import com.yedu.backend.domain.teacher.domain.repository.TeacherEnglishRepository;
import com.yedu.backend.domain.teacher.domain.repository.TeacherMathRepository;
import com.yedu.backend.domain.teacher.domain.repository.TeacherRepository;
import com.yedu.backend.global.exception.teacher.InActiveTeacherException;
import com.yedu.backend.global.exception.teacher.NotFoundEnglishTeacherException;
import com.yedu.backend.global.exception.teacher.NotFoundMathTeacherException;
import com.yedu.backend.global.exception.teacher.TeacherLoginFailException;
import com.yedu.backend.global.exception.teacher.TeacherNotFoundByIdException;
import com.yedu.backend.global.exception.teacher.TeacherNotFoundByNameAndNickNameException;
import com.yedu.backend.global.exception.teacher.TeacherNotFoundByPhoneNumberException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherGetService {
  private final TeacherRepository teacherRepository;
  private final TeacherEnglishRepository englishRepository;
  private final TeacherMathRepository mathRepository;
  private final TeacherAvailableRepository availableRepository;
  private final TeacherDistrictRepository districtRepository;

  public Teacher byNameAndNickName(String name, String nickName) {
    return teacherRepository
        .findByTeacherInfo_NameAndTeacherInfo_NickName(name, nickName)
        .orElseThrow(() -> new TeacherNotFoundByNameAndNickNameException(name, nickName));
  }

  public Teacher byPhoneNumber(String phoneNumber) {
    return teacherRepository
        .findByTeacherInfo_PhoneNumber(phoneNumber)
        .orElseThrow(() -> new TeacherNotFoundByPhoneNumberException(phoneNumber));
  }

  public Teacher byId(long teacherId) {
    return teacherRepository
        .findById(teacherId)
        .orElseThrow(() -> new TeacherNotFoundByIdException(teacherId));
  }

  public TeacherEnglish englishByTeacher(Teacher teacher) {
    return englishRepository
        .findByTeacher(teacher)
        .orElseThrow(() -> new NotFoundEnglishTeacherException(teacher.getTeacherId()));
  }

  public TeacherMath mathByTeacher(Teacher teacher) {
    return mathRepository
        .findByTeacher(teacher)
        .orElseThrow(() -> new NotFoundMathTeacherException(teacher.getTeacherId()));
  }

  public List<TeacherAvailable> availablesByTeacher(Teacher teacher) {
    return availableRepository.allAvailableByTeacher(teacher);
  }

  public List<TeacherDistrict> districtsByTeacher(Teacher teacher) {
    return districtRepository.findAllByTeacher(teacher);
  }

  public TeacherWithAvailable applicationFormTeachers(ApplicationForm applicationForm) {
    Map<Teacher, List<TeacherAvailable>> dto =
        teacherRepository.findAllMatchingApplicationForm(applicationForm);

    return new TeacherWithAvailable(dto);
  }

  public Teacher byNameAndPhoneNumber(String name, String phoneNumber) {
    Teacher teacher =
        teacherRepository
            .findByTeacherInfo_NameAndTeacherInfo_PhoneNumber(name, phoneNumber)
            .orElseThrow(() -> new TeacherLoginFailException(name, phoneNumber));
    if (!teacher.isActive()) {
      throw new InActiveTeacherException(teacher.getTeacherId());
    }
    return teacher;
  }

  public List<Teacher> remindTeachers() {
    return teacherRepository.getRemindTeacher();
  }
}
