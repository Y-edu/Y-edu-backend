package com.yedu.backend.global.event.mapper;

import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.entity.ClassSchedule;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.backend.global.event.dto.ApplyAgreeEvent;
import com.yedu.backend.global.event.dto.InviteMatchingChannelInfoEvent;
import com.yedu.backend.global.event.dto.MatchingAcceptCaseInfoEvent;
import com.yedu.backend.global.event.dto.MatchingConfirmTeacherEvent;
import com.yedu.backend.global.event.dto.MatchingParentsEvent;
import com.yedu.backend.global.event.dto.MatchingRefuseCaseDistrictEvent;
import com.yedu.backend.global.event.dto.MatchingRefuseCaseEvent;
import com.yedu.backend.global.event.dto.MatchingRefuseCaseNowEvent;
import com.yedu.backend.global.event.dto.NotifyCallingEvent;
import com.yedu.backend.global.event.dto.NotifyClassInfoEvent;
import com.yedu.backend.global.event.dto.ParentsClassInfoEvent;
import com.yedu.backend.global.event.dto.PhotoHurryEvent;
import com.yedu.backend.global.event.dto.PhotoSubmitEvent;
import com.yedu.backend.global.event.dto.RecommendGuideEvent;
import com.yedu.backend.global.event.dto.RecommendTeacherEvent;
import com.yedu.backend.global.event.dto.TeacherClassRemindEvent;
import com.yedu.backend.global.event.dto.TeacherExchangeEvent;

import java.util.List;

import static com.yedu.backend.global.event.dto.MatchingParentsEvent.*;
import static com.yedu.backend.global.event.dto.MatchingConfirmTeacherEvent.*;

public class EventMapper {
    public static RecommendTeacherEvent mapToRecommendTeacherEvent(ClassMatching classMatching) {
        ApplicationForm applicationForm = classMatching.getApplicationForm();
        Parents parents = applicationForm.getParents();
        Teacher teacher = classMatching.getTeacher();

        return new RecommendTeacherEvent(
                parents.getPhoneNumber(),
                teacher.getTeacherInfo().getNickName(),
                applicationForm.getDistrict(),
                applicationForm.getWantedSubject(),
                teacher.getTeacherId()
        );
    }

    public static NotifyCallingEvent mapToNotifyCallingEvent(Parents parents) {
        return new NotifyCallingEvent(parents.getPhoneNumber());
    }

    public static PhotoSubmitEvent mapToPhotoSubmitEvent(Teacher teacher) {
        return new PhotoSubmitEvent(teacher.getTeacherInfo().getPhoneNumber());
    }

    public static PhotoHurryEvent mapToPhotoHurryEvent(Teacher teacher) {
        return new PhotoHurryEvent(teacher.getTeacherInfo().getPhoneNumber());
    }

    public static ApplyAgreeEvent mapToApplyAgreeEvent(Teacher teacher) {
        return new ApplyAgreeEvent(teacher.getTeacherInfo().getPhoneNumber());
    }

    public static InviteMatchingChannelInfoEvent mapToInviteMatchingChannelInfoEvent(Teacher teacher) {
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        return new InviteMatchingChannelInfoEvent(
                teacherInfo.getName(),
                teacherInfo.getNickName(),
                teacherInfo.getPhoneNumber()
        );
    }

    public static NotifyClassInfoEvent mapToNotifyClassInfoEvent(ClassMatching classMatching) {
        ApplicationForm applicationForm = classMatching.getApplicationForm();
        Teacher teacher = classMatching.getTeacher();
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        return new NotifyClassInfoEvent(
                applicationForm.getOnline(),
                applicationForm.getApplicationFormId(),
                teacherInfo.getNickName(),
                applicationForm.getWantedSubject(),
                applicationForm.getDistrict(),
                applicationForm.getDong(),
                teacher.getTeacherId(),
                teacherInfo.getPhoneNumber()
        );
    }

    public static MatchingAcceptCaseInfoEvent mapToMatchingAcceptCaseEvent(ClassMatching classMatching) {
        ApplicationForm applicationForm = classMatching.getApplicationForm();
        TeacherInfo teacherInfo = classMatching.getTeacher().getTeacherInfo();
        return new MatchingAcceptCaseInfoEvent(
                applicationForm.getOnline(),
                applicationForm.getWantedSubject(),
                applicationForm.getDistrict(),
                applicationForm.getDong(),
                applicationForm.getAge(),
                teacherInfo.getPhoneNumber()
        );
    }

    public static MatchingRefuseCaseEvent mapToMatchingRefuseCaseEvent(Teacher teacher) {
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        return new MatchingRefuseCaseEvent(
                teacherInfo.getNickName(),
                teacher.getTeacherInfo().getPhoneNumber()
        );
    }

    public static MatchingRefuseCaseNowEvent mapToMatchingRefuseCaseNowEvent(Teacher teacher) {
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        return new MatchingRefuseCaseNowEvent(
                teacherInfo.getNickName(),
                teacher.getTeacherInfo().getPhoneNumber()
        );
    }

    public static MatchingRefuseCaseDistrictEvent mapToMatchingRefuseCaseDistrictEvent(Teacher teacher) {
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        return new MatchingRefuseCaseDistrictEvent(
                teacherInfo.getNickName(),
                teacher.getTeacherInfo().getPhoneNumber()
        );
    }

    public static RecommendGuideEvent mapToRecommendGuideEvent(String phoneNumber) {
        return new RecommendGuideEvent(phoneNumber);
    }

    public static ParentsClassInfoEvent mapToParentsClassInfoEvent(ClassManagement classManagement) {
        ClassMatching classMatching = classManagement.getClassMatching();
        Teacher teacher = classMatching.getTeacher();
        Parents parents = classMatching.getApplicationForm().getParents();
        return new ParentsClassInfoEvent(
                teacher.getTeacherInfo().getNickName(),
                classManagement.getSchedules()
                        .stream()
                        .map(EventMapper::mapToClassTime)
                        .toList(),
                mapToFirstDay(classManagement),
                classManagement.getTextbook(),
                parents.getPhoneNumber()
        );
    }

    private static ParentsClassInfoEvent.ClassTime mapToClassTime(ClassSchedule classSchedule) {
        return new ParentsClassInfoEvent.ClassTime(
                classSchedule.getDay().toString(),
                classSchedule.getClassTime().getStart(),
                classSchedule.getClassTime().getClassMinute()
        );
    }

    private static ParentsClassInfoEvent.FirstDay mapToFirstDay(ClassManagement classManagement) {
        return new ParentsClassInfoEvent.FirstDay(classManagement.getFirstDay(), classManagement.getClassTime().getStart());
    }

    public static TeacherClassRemindEvent mapToTeacherClassRemindEvent(ClassManagement classManagement) {
        Teacher teacher = classManagement.getClassMatching().getTeacher();
        return new TeacherClassRemindEvent(teacher.getTeacherInfo().getNickName(), teacher.getTeacherInfo().getPhoneNumber(), classManagement.getClassManagementId());
    }

    public static TeacherExchangeEvent mapToTeacherExchangeEvent(String key, ClassManagement classManagement) {
        ClassMatching classMatching = classManagement.getClassMatching();
        Teacher teacher = classMatching.getTeacher();
        ApplicationForm applicationForm = classMatching.getApplicationForm();
        Parents parents = applicationForm.getParents();
        return new TeacherExchangeEvent(
                key,
                applicationForm.getClassCount(),
                applicationForm.getClassTime(),
                applicationForm.getAge(),
                applicationForm.getDistrict().getDescription(),
                applicationForm.getPay(),
                parents.getPhoneNumber(),
                teacher.getTeacherInfo().getPhoneNumber(),
                key);
    }

    public static MatchingParentsEvent mapToMatchingParentsEvent(ClassManagement classManagement) {
        ParentsExchangeEvent parentsExchangeEvent = mapToParentsExchangeEvent(classManagement);
        ParentsClassNoticeEvent parentsClassNoticeEvent = mapToParentsClassNoticeEvent(classManagement);
        return new MatchingParentsEvent(parentsExchangeEvent, parentsClassNoticeEvent);
    }

    private static ParentsExchangeEvent mapToParentsExchangeEvent(ClassManagement classManagement) {
        ClassMatching classMatching = classManagement.getClassMatching();
        Teacher teacher = classMatching.getTeacher();
        ApplicationForm applicationForm = classMatching.getApplicationForm();
        Parents parents = applicationForm.getParents();
        return new ParentsExchangeEvent(teacher.getTeacherInfo().getNickName(), teacher.getTeacherInfo().getPhoneNumber(), parents.getPhoneNumber());
    }

    private static ParentsClassNoticeEvent mapToParentsClassNoticeEvent(ClassManagement classManagement) {
        ApplicationForm applicationForm = classManagement.getClassMatching().getApplicationForm();
        Parents parents = applicationForm.getParents();
        return new ParentsClassNoticeEvent(parents.getPhoneNumber());
    }

    public static MatchingConfirmTeacherEvent mapToMatchingConfirmTeacherEvent(ClassManagement classManagement) {
        ClassGuideEvent classGuideEvent = mapToClassGuideEvent(classManagement);
        IntroduceFinishTalkEvent introduceFinishTalkEvent = mapToFinishTalkEvent(classManagement);
        IntroduceWriteFinishTalkEvent introduceWriteFinishTalkEvent = mapToWriteFinishTalkEvent(classManagement);
        return new MatchingConfirmTeacherEvent(classGuideEvent, introduceFinishTalkEvent, introduceWriteFinishTalkEvent);
    }

    private static ClassGuideEvent mapToClassGuideEvent(ClassManagement classManagement) {
        ClassMatching classMatching = classManagement.getClassMatching();
        Teacher teacher = classMatching.getTeacher();
        return new ClassGuideEvent(teacher.getTeacherInfo().getPhoneNumber());
    }

    private static IntroduceFinishTalkEvent mapToFinishTalkEvent(ClassManagement classManagement) {
        ClassMatching classMatching = classManagement.getClassMatching();
        Teacher teacher = classMatching.getTeacher();
        return new IntroduceFinishTalkEvent(teacher.getTeacherInfo().getPhoneNumber());
    }

    private static IntroduceWriteFinishTalkEvent mapToWriteFinishTalkEvent(ClassManagement classManagement) {
        ApplicationForm applicationForm = classManagement.getClassMatching().getApplicationForm();
        List<ClassSchedule> schedules = classManagement.getSchedules();
        ClassSchedule classSchedule = schedules.get(0);
        Teacher teacher = classManagement.getClassMatching().getTeacher();
        return new IntroduceWriteFinishTalkEvent(applicationForm.getApplicationFormId(), classManagement.getSchedules().size(), classSchedule.getClassTime().getClassMinute(), teacher.getTeacherInfo().getPhoneNumber());
    }
}
