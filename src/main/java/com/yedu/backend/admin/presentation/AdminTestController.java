package com.yedu.backend.admin.presentation;

import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.domain.matching.application.mapper.ClassMatchingMapper;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.repository.ClassMatchingRepository;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.repository.ApplicationFormRepository;
import com.yedu.backend.domain.parents.domain.repository.GoalRepository;
import com.yedu.backend.domain.parents.domain.repository.ParentsRepository;
import com.yedu.backend.domain.parents.domain.service.ParentsGetService;
import com.yedu.backend.domain.parents.domain.service.ParentsSaveService;
import com.yedu.backend.domain.teacher.domain.entity.*;
import com.yedu.backend.domain.teacher.domain.entity.constant.*;
import com.yedu.backend.domain.teacher.domain.repository.*;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import com.yedu.backend.domain.teacher.domain.service.TeacherSaveService;
import com.yedu.backend.global.bizppurio.application.usecase.BizppurioParentsMessage;
import com.yedu.backend.global.bizppurio.application.usecase.BizppurioTeacherMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "ADMIN TEST Controller", description = "여러가지 테스트용 API입니다")
@Transactional
public class AdminTestController {
    private final TeacherSaveService teacherSaveService;
    private final BizppurioTeacherMessage teacherMessage;
    private final TeacherGetService teacherGetService;
    private final BizppurioParentsMessage parentsMessage;
    private final ParentsGetService parentsGetService;
    private final AdminGetService adminGetService;
    private final ParentsSaveService parentsSaveService;
    private final ApplicationFormRepository applicationFormRepository;
    private final ParentsRepository parentsRepository;
    private final GoalRepository goalRepository;
    private final ClassMatchingRepository classMatchingRepository;
    private final TeacherAvailableRepository teacherAvailableRepository;
    private final TeacherDistrictRepository teacherDistrictRepository;
    private final TeacherEnglishRepository teacherEnglishRepository;
    private final TeacherMathRepository teacherMathRepository;
    private final TeacherRepository teacherRepository;

    @PostMapping("/test/teacher/signup/{phoneNumber}")
    @Operation(summary = "선생님 간편 가입 - 전화번호를 넣어주세요 (간편가입이라 내용은 기대하지 마세요)")
    public void signUpTeacher(@PathVariable String phoneNumber) {
        Teacher teacher = Teacher.builder()
                .teacherInfo(new TeacherInfo("채영대", "토마스", "a@a.com", phoneNumber, "1998", TeacherGender.남, "", ""))
                .teacherClassInfo(new TeacherClassInfo("나는 이러한 선생님", TeachingStyle.CARING, TeachingStyle.FUN, "저런 방식", "저런 방식", "# 이런 학생", "이러이러하다", true, true))
                .teacherSchoolInfo(new TeacherSchoolInfo("대학교", true, "학과", "고등학교", "외고"))
                .source("기타")
                .status(TeacherStatus.활동중)
                .build();
        TeacherEnglish teacherEnglish = TeacherEnglish.builder()
                .teacher(teacher)
                .foreignExperience("# abc")
                .teachingExperience("# abc")
                .teachingHistory(3)
                .teachingStyle("이러이러")
                .build();
        TeacherMath teacherMath = TeacherMath.builder()
                .teacher(teacher)
                .teachingExperience("# abc")
                .teachingHistory(3)
                .teachingStyle("이러이러")
                .build();
        teacherSaveService.saveTeacher(teacher, List.of(TeacherAvailable.builder()
                .teacher(teacher)
                .day(Day.byInt(2))
                .availableTime(LocalTime.parse("12:00"))
                .build()), List.of(TeacherDistrict.builder().teacher(teacher).district(District.강남구).build()), teacherEnglish, teacherMath);
    }

    @PostMapping("/test/parents/signup/{phoneNumber}")
    @Operation(summary = "학부모 간편 가입 - 전화번호를 넣어주세요 (간편가입이라 내용은 기대하지 마세요)")
    public void signUpParents(@PathVariable String phoneNumber) {
        Parents parents = Parents.builder()
                .phoneNumber(phoneNumber)
                .marketingAgree(true)
                .build();
        parentsSaveService.saveParents(parents);
    }

    @DeleteMapping("/test/teacher/{phoneNumber}")
    @Operation(summary = "선생님 간편 삭제 - 전화번호를 넣어주세요")
    public void deleteTeacher(@PathVariable String phoneNumber) {
        classMatchingRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
        teacherAvailableRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
        teacherDistrictRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
        teacherEnglishRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
        teacherEnglishRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
        teacherMathRepository.deleteAllByTeacher_TeacherInfo_PhoneNumber(phoneNumber);
        teacherRepository.deleteAllByTeacherInfo_PhoneNumber(phoneNumber);
    }

    @DeleteMapping("/test/parents/{phoneNumber}")
    @Operation(summary = "학부모 간편 삭제 - 전화번호를 넣어주세요")
    public void deleteParents(@PathVariable String phoneNumber) {
        classMatchingRepository.deleteAllByApplicationForm_Parents_PhoneNumber(phoneNumber);
        goalRepository.deleteAllByApplicationForm_Parents_PhoneNumber(phoneNumber);
        applicationFormRepository.deleteAllByParents_PhoneNumber(phoneNumber);
        parentsRepository.deleteAllByPhoneNumber(phoneNumber);
    }
    @PostMapping("/test/teacher/profile/{phoneNumber}")
    @Operation(summary = "구글폼 프로필 작성시 받는 알림톡 - 받을 사람의 전화번호를 적어주세요! 단, 이미 가입은 이미 선생님으로 했어야 합니다!")
    public void finProfile(@PathVariable String phoneNumber) {
        Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
        teacherMessage.counselStartAndPhotoSubmit(teacher);
    }

    @PostMapping("/test/teacher/photo/{phoneNumber}")
    @Operation(summary = "구글폼 사진 및 영상 제출시 받는 알림톡 - 받을 사람의 전화번호를 적어주세요! 단, 이미 이미 선생님으로 가입은 했어야 합니다!")
    public void finPhoto(@PathVariable String phoneNumber) {
        Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
        teacherMessage.applyAgree(teacher);
    }

    @PostMapping("/test/teacher/agree/{phoneNumber}")
    @Operation(summary = "계약서 제출시 받는 알림톡 - 받을 사람의 전화번호를 적어주세요! 단, 이미 이미 선생님으로 가입은 했어야 합니다!")
    public void finApplyAgree(@PathVariable String phoneNumber) {
        Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
        teacherMessage.matchingChannel(teacher);
    }

    @PostMapping("/test/teacher/recommend/{applicationFormId}/{phoneNumber}")
    @Operation(summary = "선생님에게 수업 추천해주는 알림톡 - 관리자 페이지 확인 후 신청건ID(EX. 온라인11a)와 받을 사람의 전화번호를 적어주세요! 단, 이미 이미 선생님으로 가입은 했어야 합니다!")
    public void recommendClass(@PathVariable String applicationFormId, @PathVariable String phoneNumber) {
        ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
        Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);

        Optional<ClassMatching> existingClassMatching = classMatchingRepository.findByApplicationForm_ApplicationFormIdAndTeacher_TeacherIdAndTeacher_TeacherInfo_PhoneNumber(
                applicationForm.getApplicationFormId(),
                teacher.getTeacherId(),
                teacher.getTeacherInfo().getPhoneNumber()
        );
        existingClassMatching.ifPresentOrElse(
                classMatching -> {}, // 기존 매칭이 존재하면 아무 작업 안 함
                () -> {
                    ClassMatching newClassMatching = ClassMatchingMapper.mapToClassMatching(teacher, applicationForm);
                    classMatchingRepository.save(newClassMatching);
                }
        );

        teacherMessage.notifyClass(applicationForm, teacher);
    }
    // 실제로 매칭을 하나 생성해줘야함

    @PostMapping("/test/teacher/accept/{applicationFormId}/{phoneNumber}")
    @Operation(summary = "선생님이 수업 신청시 받는 알림톡 - 관리자 페이지 확인 후 신청건ID(EX. 온라인11a)와 받을 사람의 전화번호를 적어주세요! 단, 이미 선생님으로 이미 가입은 했어야 합니다!")
    public void acceptCase(@PathVariable String phoneNumber, @PathVariable String applicationFormId) {
        ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
        Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
        teacherMessage.acceptCase(applicationForm, teacher);
    }

    @PostMapping("/test/teacher/refuse/{phoneNumber}")
    @Operation(summary = "선생님이 수업 거절시 받는 알림톡 - 관리자 페이지 확인 후 신청건ID(EX. 온라인11a)와 받을 사람의 전화번호를 적어주세요! 단, 이미 선생님으로 가입은 했어야 합니다!")
    public void refuseCase(@PathVariable String phoneNumber) {
        Teacher teacher = teacherGetService.byPhoneNumber(phoneNumber);
        teacherMessage.refuseCase(teacher);
    }

    @PostMapping("/test/parents/apply/{phoneNumber}")
    @Operation(summary = "학부모 탈리 폼 작성시 받는 알림톡 - 받을 사람의 전화번호를 적어주세요! 단, 학부모로 이미 가입은 했어야 합니다!")
    public void finApplicationForm(@PathVariable String phoneNumber) {
        Parents parents = parentsGetService.optionalParentsByPhoneNumber(phoneNumber)
                .orElseThrow();
        parentsMessage.notifyCalling(parents);
    }
}
