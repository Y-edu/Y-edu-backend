package com.yedu.backend.domain.teacher.application.usecase;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherContractRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherProfileFormRequest;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import com.yedu.backend.domain.teacher.domain.service.TeacherSaveService;
import com.yedu.backend.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.backend.global.bizppurio.application.usecase.BizppurioTeacherMessage;
import com.yedu.backend.global.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.yedu.backend.domain.teacher.application.mapper.TeacherMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeacherManageUseCase {
    private final TeacherSaveService teacherSaveService;
    private final TeacherGetService teacherGetService;
    private final TeacherUpdateService teacherUpdateService;
    private final S3UploadService s3UploadService;
    private final BizppurioTeacherMessage bizppurioTeacherMessage;

    public void saveTeacher(TeacherInfoFormRequest request) {
        Teacher teacher = mapToTeacher(request); // 기본 선생님 정보
        List<TeacherDistrict> teacherDistricts = getTeacherDistricts(request, teacher);
        List<TeacherAvailable> teacherAvailables = getTeacherAvailables(request, teacher);
        TeacherEnglish english = getTeacherEnglish(request, teacher);
        TeacherMath math = getTeacherMath(request, teacher);
        teacherSaveService.saveTeacher(teacher, teacherAvailables, teacherDistricts, english, math);

        // 선생님 등록 1 알림톡 전송, 선생님 등록 2 알림톡 전송
        bizppurioTeacherMessage.counselStartAndPhotoSubmit(teacher);
    }

    private TeacherMath getTeacherMath(TeacherInfoFormRequest request, Teacher teacher) {
        TeacherMath math = null;
        if (request.mathPossible())
            math = mapToTeacherMath(teacher, request);
        return math;
    }

    private TeacherEnglish getTeacherEnglish(TeacherInfoFormRequest request, Teacher teacher) {
        TeacherEnglish english = null;
        if (request.englishPossible())
            english = mapToTeacherEnglish(teacher, request);
        return english;
    }

    private List<TeacherAvailable> getTeacherAvailables(TeacherInfoFormRequest request, Teacher teacher) {
        List<List<String>> availables = request.available();
        List<TeacherAvailable> teacherAvailables = new ArrayList<>();

        for (int day = 0; day < availables.size(); day++) {
            int finalDay = day;
            List<String> times = availables.get(day);
            times.stream()
                    .filter(time -> !time.equals("불가"))  // "불가"가 아닌 값만 필터링
                    .forEach(time -> {
                        TeacherAvailable teacherAvailable = mapToTeacherAvailable(teacher, finalDay, time);
                        teacherAvailables.add(teacherAvailable);
                    });
        }
        // 선생님 가능 시간 : teacherAvailables 생성
        return teacherAvailables;
    }

    private List<TeacherDistrict> getTeacherDistricts(TeacherInfoFormRequest request, Teacher teacher) {
        return request.region()
                .stream()
                .map(region -> mapToTeacherDistrict(teacher, region))
                .toList(); // 선생님 교육 가능 구역
    }

    public void saveTeacherProfile(MultipartFile profile, TeacherProfileFormRequest request) {
        String profileUrl = s3UploadService.saveProfileFile(profile);
        Teacher teacher = teacherGetService.byPhoneNumber(request.phoneNumber());
        log.info("profile 사진 저장");
        teacherUpdateService.updateProfile(teacher, profileUrl);
        bizppurioTeacherMessage.applyAgree(teacher);
    }

    public void submitContract(TeacherContractRequest request) {
        Teacher teacher = teacherGetService.byPhoneNumber(request.phoneNumber());
        teacherUpdateService.updateActive(teacher);
        bizppurioTeacherMessage.matchingChannel(teacher);
        bizppurioTeacherMessage.applyChannel(teacher);
    }

    public List<Teacher> notifyClass(ApplicationForm applicationForm) {
        List<Teacher> teachers = teacherGetService.applicationFormTeachers(applicationForm);
        teachers.forEach(teacher -> {
            teacherUpdateService.updateAlertCount(teacher);
            bizppurioTeacherMessage.notifyClass(applicationForm, teacher);
        });
        return teachers;
    }
}
