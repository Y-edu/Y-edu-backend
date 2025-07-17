package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.global.exception.matching.MatchingNotFoundException;
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

  public List<ClassMatching> getMatched(Teacher teacher) {
    return classMatchingRepository.findByTeacherAndMatchStatus(teacher, MatchingStatus.최종매칭);
  }

  public List<ClassMatching> getPaused(Teacher teacher) {
    return classMatchingRepository.findByTeacherAndMatchStatus(teacher, MatchingStatus.일시중단);
  }

  public ClassMatching getBySessionId(Long sessionId) {
    return classMatchingRepository
        .findBySessionId(sessionId)
        .orElseThrow(() -> new IllegalArgumentException("매칭 정보를 찾을수가 없습니다"));
  }
}
