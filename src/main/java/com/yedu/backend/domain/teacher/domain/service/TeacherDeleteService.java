package com.yedu.backend.domain.teacher.domain.service;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.repository.TeacherDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherDeleteService {
    private final TeacherDistrictRepository districtRepository;

    public void allByTeacher(Teacher teacher) {
        districtRepository.deleteAllByTeacher(teacher);
    }
}
