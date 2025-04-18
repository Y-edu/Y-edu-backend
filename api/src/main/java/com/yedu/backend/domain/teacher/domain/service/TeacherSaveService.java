package com.yedu.backend.domain.teacher.domain.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherSaveService {
    private final TeacherAvailableRepository availableRepository;
    private final TeacherDistrictRepository districtRepository;
    private final TeacherEnglishRepository englishRepository;
    private final TeacherMathRepository mathRepository;
    private final TeacherRepository teacherRepository;

    public void saveTeacher(
        Teacher teacher, List<TeacherAvailable> availables, List<TeacherDistrict> districts, TeacherEnglish english, TeacherMath math) {
        teacherRepository.save(teacher);
        availables.forEach(availableRepository::save);
        districts.forEach(districtRepository::save);
        if (english != null)
            englishRepository.save(english);
        if (math != null)
            mathRepository.save(math);
    }

    public void saveDistricts(List<TeacherDistrict> districts) {
        districts.forEach(districtRepository::save);
    }

    public void saveAvailable(List<TeacherAvailable> availables) {
        availableRepository.saveAll(availables);
    }
}
