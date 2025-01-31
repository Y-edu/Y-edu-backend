package com.yedu.backend.domain.teacher.domain.service;

import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherGetService {
    private final TeacherRepository teacherRepository;

    public Teacher byPhoneNumber(String phoneNumber) {
        return teacherRepository.findByTeacherInfo_PhoneNumber(phoneNumber)
                .orElseThrow();
    }
}
