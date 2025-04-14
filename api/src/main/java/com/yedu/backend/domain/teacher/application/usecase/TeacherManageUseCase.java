package com.yedu.backend.domain.teacher.application.usecase;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.teacher.application.dto.req.AlarmTalkChangeRequest;
import com.yedu.backend.domain.teacher.application.dto.req.AvailableChangeRequest;
import com.yedu.backend.domain.teacher.application.dto.req.DistrictChangeRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherContractRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherInfoFormRequest;
import com.yedu.backend.domain.teacher.application.dto.req.TeacherProfileFormRequest;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.backend.domain.teacher.domain.entity.TeacherEnglish;
import com.yedu.backend.domain.teacher.domain.entity.TeacherMath;
import com.yedu.backend.domain.teacher.domain.service.TeacherDeleteService;
import com.yedu.backend.domain.teacher.domain.service.TeacherGetService;
import com.yedu.backend.domain.teacher.domain.service.TeacherSaveService;
import com.yedu.backend.domain.teacher.domain.service.TeacherUpdateService;
import com.yedu.backend.global.config.s3.S3UploadService;
import com.yedu.backend.global.event.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.yedu.backend.domain.teacher.application.mapper.TeacherMapper.*;
import static com.yedu.backend.global.event.mapper.BizppurioEventMapper.*;
import static com.yedu.backend.global.event.mapper.DiscordEventMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeacherManageUseCase {
    private final TeacherSaveService teacherSaveService;
    private final TeacherGetService teacherGetService;
    private final TeacherUpdateService teacherUpdateService;
    private final TeacherDeleteService teacherDeleteService;
    private final S3UploadService s3UploadService;
    private final EventPublisher eventPublisher;

    public void saveTeacher(TeacherInfoFormRequest request) {
        Teacher teacher = mapToTeacher(request); // 기본 선생님 정보
        List<TeacherDistrict> teacherDistricts = getTeacherDistricts(request, teacher);
        List<TeacherAvailable> teacherAvailables = getTeacherAvailables(request.available(), teacher);
        TeacherEnglish english = getTeacherEnglish(request, teacher);
        TeacherMath math = getTeacherMath(request, teacher);
        teacherSaveService.saveTeacher(teacher, teacherAvailables, teacherDistricts, english, math);

        // 선생님 등록 1 알림톡 전송, 선생님 등록 2 알림톡 전송
        eventPublisher.publishPhotoSubmitEvent(mapToPhotoSubmitEvent(teacher));
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

    private List<TeacherAvailable> getTeacherAvailables(List<List<String>> available, Teacher teacher) {
        List<TeacherAvailable> teacherAvailables = new ArrayList<>();

        for (int day = 0; day < available.size(); day++) {
            int finalDay = day;
            List<String> times = available.get(day);
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
        teacherUpdateService.updateProfile(teacher, profileUrl);
        teacherUpdateService.updateFormStep(teacher);
        eventPublisher.publishApplyAgreeEvent(mapToApplyAgreeEvent(teacher));
    }

    public void submitContract(TeacherContractRequest request) {
        Teacher teacher = teacherGetService.byPhoneNumber(request.phoneNumber());
        List<TeacherDistrict> teacherDistricts = teacherGetService.districtsByTeacher(teacher);
        teacherUpdateService.updateActive(teacher);
        eventPublisher.publishInviteMatchingChannelInfoEvent(mapToInviteMatchingChannelInfoEvent(teacher));
        eventPublisher.publishTeacherRegisterEvent(mapToTeacherRegisterEvent(teacher, teacherDistricts));
    }

    public void notifyClass(List<ClassMatching> classMatchings) {
        classMatchings.forEach(classMatching -> {
            Teacher teacher = classMatching.getTeacher();
            teacherUpdateService.updateAlertCount(teacher);
            eventPublisher.publishNotifyClassInfoEvent(mapToNotifyClassInfoEvent(classMatching));
        });
    }

    public void changeAlarmTalkStatus(AlarmTalkChangeRequest request) {
        Teacher teacher = teacherGetService.byNameAndPhoneNumber(request.name(), request.phoneNumber());
        teacherUpdateService.updateStatus(teacher, request.alarmTalk());
    }

    public void changeDistrict(DistrictChangeRequest request) {
        Teacher teacher = teacherGetService.byNameAndPhoneNumber(request.name(), request.phoneNumber());
        teacherDeleteService.districtByTeacher(teacher);
        List<TeacherDistrict> districts = request.districts().stream()
                .map(region -> mapToTeacherDistrict(teacher, region))
                .toList();
        teacherSaveService.saveDistricts(districts);
    }

    public void changeAvailable(AvailableChangeRequest request) {
        Teacher teacher = teacherGetService.byNameAndPhoneNumber(request.name(), request.phoneNumber());
        teacherDeleteService.availableByTeacher(teacher);
        List<TeacherAvailable> availables = getTeacherAvailables(request.available(), teacher);
        teacherSaveService.saveAvailable(availables);
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void remindAlarm() {
        log.info("리마인드 알림톡 전송 시작");
        teacherGetService.remindTeachers()
                .forEach(teacher -> {
                    log.info("teacherId : " + teacher.getTeacherId() + " 리마인드 알림톡 전송");
                    teacherUpdateService.updateRemind(teacher);
                    eventPublisher.publishPhotoHurryEvent(mapToPhotoHurryEvent(teacher));
                });
    }
}
