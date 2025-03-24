package com.yedu.backend.domain.teacher.domain.service;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.repository.*;
import com.yedu.backend.global.exception.teacher.*;
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
                .orElseThrow(TeacherNotFoundByNameAndNickNameException::new);
    }

    public Teacher byPhoneNumber(String phoneNumber) {
        return teacherRepository.findByTeacherInfo_PhoneNumber(phoneNumber)
                .orElseThrow(TeacherNotFoundByPhoneNumberException::new);
    }

    public Teacher byId(long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(TeacherNotFoundByIdException::new);
    }

    public TeacherEnglish englishByTeacher(Teacher teacher) {
        return englishRepository.findByTeacher(teacher)
                .orElseThrow(NotFoundEnglishTeacherException::new);
    }

    public TeacherMath mathByTeacher(Teacher teacher) {
        return mathRepository.findByTeacher(teacher)
                .orElseThrow(NotFoundMathTeacherException::new);
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
                .orElseThrow(TeacherLoginFailException::new);
        if (!teacher.isActive()) {
            throw new InActiveTeacherException();
        }
        return teacher;
    }

}
