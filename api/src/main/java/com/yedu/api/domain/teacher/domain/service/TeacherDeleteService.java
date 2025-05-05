package com.yedu.api.domain.teacher.domain.service;

import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.repository.TeacherAvailableRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherDeleteService {
  private final TeacherDistrictRepository districtRepository;
  private final TeacherAvailableRepository availableRepository;

  public void districtByTeacher(Teacher teacher) {
    districtRepository.deleteAllByTeacher(teacher);
  }

  public void availableByTeacher(Teacher teacher) {
    availableRepository.deleteAllByTeacher(teacher);
  }
}
