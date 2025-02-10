package com.yedu.backend.admin.domain.service;

import com.yedu.backend.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.admin.domain.repository.AdminRepository;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.backend.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.backend.domain.parents.domain.repository.GoalRepository;
import com.yedu.backend.domain.parents.domain.repository.ParentsRepository;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.backend.domain.teacher.domain.repository.TeacherDistrictRepository;
import com.yedu.backend.domain.teacher.domain.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminGetService {
    private final ApplicationFormRepository applicationFormRepository;
    private final ClassMatchingRepository classMatchingRepository;
    private final GoalRepository goalRepository;
    private final ParentsRepository parentsRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherDistrictRepository teacherDistrictRepository;
    private final AdminRepository adminRepository;

    public Parents parentsById(Long parentsId) {
        return parentsRepository.findById(parentsId)
                .orElseThrow();
    }

    public List<ApplicationForm> allApplication() {
        return applicationFormRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<ClassMatching> allMatching(ApplicationForm applicationForm) {
        return classMatchingRepository.allByApplicationForm(applicationForm);
    }

    public ApplicationForm applicationFormById(String applicationFormId) {
        return applicationFormRepository.findById(applicationFormId)
                .orElseThrow();
    }

    public List<Goal> allGoalByApplicationForm(ApplicationForm applicationForm) {
        return goalRepository.findAllByApplicationForm(applicationForm);
    }

    public List<Teacher> allTeacherBySearch(TeacherSearchRequest teacherSearchRequest) {
        return teacherRepository.findAllSearchTeacher(teacherSearchRequest);
    }

    public Teacher teacherById(long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow();
    }

    public List<TeacherDistrict> allDistrictByTeacher(Teacher teacher) {
        return teacherDistrictRepository.findAllByTeacher(teacher);
    }

    public Admin adminByLoginId(String id) {
        return adminRepository.findByLoginId(id)
                .orElseThrow();
    }

    public ClassMatching classMatchingById(long classMatching) {
        return classMatchingRepository.findById(classMatching)
                .orElseThrow();
    }
}
