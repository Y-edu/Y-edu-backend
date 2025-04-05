package com.yedu.backend.global.event.mapper;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.backend.global.event.dto.*;

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
}