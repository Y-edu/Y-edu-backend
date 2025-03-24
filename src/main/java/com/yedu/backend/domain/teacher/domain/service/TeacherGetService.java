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
                .orElseThrow(() -> new TeacherNotFoundByNameAndNickNameException(name, nickName));
    }

    public Teacher byPhoneNumber(String phoneNumber) {
        return teacherRepository.findByTeacherInfo_PhoneNumber(phoneNumber)
                .orElseThrow(() -> new TeacherNotFoundByPhoneNumberException(phoneNumber));
    }

    public Teacher byId(long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundByIdException(teacherId));
    }

    public TeacherEnglish englishByTeacher(Teacher teacher) {
        return englishRepository.findByTeacher(teacher)
                .orElseThrow(() -> new NotFoundEnglishTeacherException(teacher.getTeacherId()));
    }

    public TeacherMath mathByTeacher(Teacher teacher) {
        return mathRepository.findByTeacher(teacher)
                .orElseThrow(() -> new NotFoundMathTeacherException(teacher.getTeacherId()));
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
                .orElseThrow(() -> new TeacherLoginFailException(name, phoneNumber));
        if (!teacher.isActive()) {
            throw new InActiveTeacherException(teacher.getTeacherId());
        }
        return teacher;
    }

}
