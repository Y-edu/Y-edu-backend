package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.global.exception.matching.MatchingNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassMatchingGetService {
  private final ClassMatchingRepository classMatchingRepository;

  public ClassMatching classMatchingByApplicationFormIdAndTeacherId(
      String applicationFormId, long teacherId, String phoneNumber) {
    return classMatchingRepository
        .findByApplicationForm_ApplicationFormIdAndTeacher_TeacherIdAndTeacher_TeacherInfo_PhoneNumber(
            applicationFormId, teacherId, phoneNumber)
        .orElseThrow(
            () -> new MatchingNotFoundException(applicationFormId, teacherId, phoneNumber));
  }

  public List<ClassMatching> getByApplicationForm(ApplicationForm applicationForm) {
    return classMatchingRepository.findByApplicationForm(applicationForm);
  }

  public ClassMatching getById(Long classMatchingId) {
    return classMatchingRepository
        .findById(classMatchingId)
        .orElseThrow(() -> new MatchingNotFoundException(classMatchingId));
  }
}
