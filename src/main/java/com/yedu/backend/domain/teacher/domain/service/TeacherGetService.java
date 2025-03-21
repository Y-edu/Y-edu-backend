package com.yedu.backend.domain.teacher.domain.service;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.repository.*;
import com.yedu.backend.domain.teacher.exception.InActiveTeacherException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherGetService {
    private final TeacherRepository teacherRepository;
    private final TeacherEnglishRepository englishRepository;
    private final TeacherMathRepository mathRepository;
    private final TeacherAvailableRepository availableRepository;
    private final TeacherDistrictRepository districtRepository;

    public Teacher byNameAndNickName(String name, String nickName) {
        return teacherRepository.findByTeacherInfo_NameAndTeacherInfo_NickName(name, nickName)
                .orElseThrow();
    }

    public Teacher byPhoneNumber(String phoneNumber) {
        return teacherRepository.findByTeacherInfo_PhoneNumber(phoneNumber)
                .orElseThrow();
    }

    public Teacher byId(long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow();
    }

    public TeacherEnglish englishByTeacher(Teacher teacher) {
        return englishRepository.findByTeacher(teacher)
                .orElseThrow();
    }

    public TeacherMath mathByTeacher(Teacher teacher) {
        return mathRepository.findByTeacher(teacher)
                .orElseThrow();
    }

    public List<TeacherAvailable> availablesByTeacher(Teacher teacher) {
        return availableRepository.allAvailableByTeacher(teacher);
    }

    public List<TeacherDistrict> districtsByTeacher(Teacher teacher) {
        return districtRepository.findAllByTeacher(teacher);
    }

    public List<Teacher> applicationFormTeachers(ApplicationForm applicationForm) {
        return teacherRepository.findAllMatchingApplicationForm(applicationForm);
    }

    public Teacher byNameAndPhoneNumber(String name, String phoneNumber) {
        Teacher teacher = teacherRepository.findByTeacherInfo_NameAndTeacherInfo_PhoneNumber(name, phoneNumber)
                .orElseThrow();
        if (!teacher.isActive()) {
            throw new InActiveTeacherException();
        }
        return teacher;
    }

}
