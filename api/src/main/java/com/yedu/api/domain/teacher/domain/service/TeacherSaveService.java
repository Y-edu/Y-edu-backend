package com.yedu.api.domain.teacher.domain.service;

import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.api.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.api.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.api.domain.teacher.domain.entity.TeacherMath;
import com.yedu.api.domain.teacher.domain.repository.TeacherAvailableRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherDistrictRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherEnglishRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherMathRepository;
import com.yedu.api.domain.teacher.domain.repository.TeacherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherSaveService {
  private final TeacherAvailableRepository availableRepository;
  private final TeacherDistrictRepository districtRepository;
  private final TeacherEnglishRepository englishRepository;
  private final TeacherMathRepository mathRepository;
  private final TeacherRepository teacherRepository;

  public void saveTeacher(
      Teacher teacher,
      List<TeacherAvailable> availables,
      List<TeacherDistrict> districts,
      TeacherEnglish english,
      TeacherMath math) {
    teacherRepository.save(teacher);
    availables.forEach(availableRepository::save);
    districts.forEach(districtRepository::save);
    if (english != null) englishRepository.save(english);
    if (math != null) mathRepository.save(math);
  }

  public void saveDistricts(List<TeacherDistrict> districts) {
    districts.forEach(districtRepository::save);
  }

  public void saveAvailable(List<TeacherAvailable> availables) {
    availables.forEach(availableRepository::save);
  }
}
