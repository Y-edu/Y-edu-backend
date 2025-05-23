package com.yedu.api.admin.domain.service;

import com.yedu.api.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.api.admin.domain.entity.Admin;
import com.yedu.api.admin.domain.repository.AdminRepository;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.api.domain.parents.domain.repository.GoalRepository;
import com.yedu.api.domain.parents.domain.repository.ParentsRepository;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.api.domain.teacher.domain.repository.TeacherDistrictRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminGetService {
  private final ApplicationFormRepository applicationFormRepository;
  private final ClassMatchingRepository classMatchingRepository;
  private final GoalRepository goalRepository;
  private final ParentsRepository parentsRepository;
  private final TeacherRepository teacherRepository;
  private final TeacherDistrictRepository teacherDistrictRepository;
  private final AdminRepository adminRepository;

  public Parents parentsById(Long parentsId) {
    return parentsRepository.findById(parentsId).orElseThrow();
  }

  public List<ApplicationForm> allApplication() {
    return applicationFormRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
  }

  public List<ClassMatching> allMatching(String applicationFormId) {
    return classMatchingRepository.allByApplicationForm(applicationFormId);
  }

  public ApplicationForm applicationFormById(String applicationFormId) {
    return applicationFormRepository.findById(applicationFormId).orElseThrow();
  }

  public List<Goal> allGoalByApplicationForm(ApplicationForm applicationForm) {
    return goalRepository.findAllByApplicationForm(applicationForm);
  }

  public List<Teacher> allTeacherBySearch(
      List<ClassMatching> classMatchings, TeacherSearchRequest teacherSearchRequest) {
    return teacherRepository.findAllSearchTeacher(classMatchings, teacherSearchRequest);
  }

  public Teacher teacherById(long teacherId) {
    return teacherRepository.findById(teacherId).orElseThrow();
  }

  public List<TeacherDistrict> allDistrictByTeacher(Teacher teacher) {
    return teacherDistrictRepository.findAllByTeacher(teacher);
  }

  public Admin adminByLoginId(String id) {
    return adminRepository.findByLoginId(id).orElseThrow();
  }

  public ClassMatching classMatchingById(long classMatching) {
    return classMatchingRepository.findById(classMatching).orElseThrow();
  }
}
