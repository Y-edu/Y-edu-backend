package com.yedu.api.global.event.mapper;

import static java.util.Comparator.comparing;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSchedule;
import com.yedu.api.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.vo.DayTime;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.common.event.bizppurio.*;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.ClassGuideEvent;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceFinishTalkEvent;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent;
import com.yedu.common.event.bizppurio.MatchingParentsEvent.ParentsClassNoticeEvent;
import com.yedu.common.event.bizppurio.MatchingParentsEvent.ParentsExchangeEvent;
import java.util.List;
import java.util.stream.Collectors;

public class BizppurioEventMapper {
  public static RecommendTeacherEvent mapToRecommendTeacherEvent(
      ClassMatching classMatching, String token) {
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    Parents parents = applicationForm.getParents();
    Teacher teacher = classMatching.getTeacher();

    return new RecommendTeacherEvent(
        parents.getPhoneNumber(),
        teacher.getTeacherInfo().getNickName(),
        applicationForm.getDistrict().getDescription(),
        applicationForm.getWantedSubject().toString(),
        teacher.getTeacherId(),
        token);
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

  public static InviteMatchingChannelInfoEvent mapToInviteMatchingChannelInfoEvent(
      Teacher teacher) {
    TeacherInfo teacherInfo = teacher.getTeacherInfo();
    return new InviteMatchingChannelInfoEvent(
        teacherInfo.getName(), teacherInfo.getNickName(), teacherInfo.getPhoneNumber());
  }

  public static NotifyClassInfoEvent mapToNotifyClassInfoEvent(
      ClassMatching classMatching, String token) {
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    Teacher teacher = classMatching.getTeacher();
    TeacherInfo teacherInfo = teacher.getTeacherInfo();
    return new NotifyClassInfoEvent(
        applicationForm.getOnline().toString(),
        applicationForm.getApplicationFormId(),
        teacherInfo.getNickName(),
        applicationForm.getWantedSubject().toString(),
        applicationForm.getDistrict().getDescription(),
        applicationForm.getDong(),
        teacher.getTeacherId(),
        teacherInfo.getPhoneNumber(),
        token);
  }

  public static MatchingAcceptCaseInfoEvent mapToMatchingAcceptCaseEvent(
      ClassMatching classMatching) {
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    TeacherInfo teacherInfo = classMatching.getTeacher().getTeacherInfo();
    return new MatchingAcceptCaseInfoEvent(
        applicationForm.getOnline().toString(),
        applicationForm.getWantedSubject().toString(),
        applicationForm.getDistrict().getDescription(),
        applicationForm.getDong(),
        applicationForm.getAge(),
        teacherInfo.getPhoneNumber());
  }

  public static MatchingRefuseCaseEvent mapToMatchingRefuseCaseEvent(Teacher teacher) {
    TeacherInfo teacherInfo = teacher.getTeacherInfo();
    return new MatchingRefuseCaseEvent(
        teacherInfo.getNickName(), teacher.getTeacherInfo().getPhoneNumber());
  }

  public static MatchingRefuseCaseNowEvent mapToMatchingRefuseCaseNowEvent(Teacher teacher) {
    TeacherInfo teacherInfo = teacher.getTeacherInfo();
    return new MatchingRefuseCaseNowEvent(
        teacherInfo.getNickName(), teacher.getTeacherInfo().getPhoneNumber());
  }

  public static MatchingRefuseCaseDistrictEvent mapToMatchingRefuseCaseDistrictEvent(
      Teacher teacher) {
    TeacherInfo teacherInfo = teacher.getTeacherInfo();
    return new MatchingRefuseCaseDistrictEvent(
        teacherInfo.getNickName(), teacher.getTeacherInfo().getPhoneNumber());
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
        classManagement.getSchedules().stream().map(BizppurioEventMapper::mapToClassTime).toList(),
        mapToFirstDay(classManagement),
        classManagement.getTextbook(),
        parents.getPhoneNumber());
  }

  private static ParentsClassInfoEvent.ClassTime mapToClassTime(ClassSchedule classSchedule) {
    return new ParentsClassInfoEvent.ClassTime(
        classSchedule.getDay().toString(),
        classSchedule.getClassTime().getStart(),
        classSchedule.getClassTime().getClassMinute());
  }

  private static ParentsClassInfoEvent.FirstDay mapToFirstDay(ClassManagement classManagement) {
    return new ParentsClassInfoEvent.FirstDay(
        classManagement.getFirstDay(), classManagement.getClassTime().getStart());
  }

  public static TeacherClassRemindEvent mapToTeacherClassRemindEvent(
      ClassManagement classManagement) {
    Teacher teacher = classManagement.getClassMatching().getTeacher();
    return new TeacherClassRemindEvent(
        teacher.getTeacherInfo().getNickName(),
        teacher.getTeacherInfo().getPhoneNumber(),
        classManagement.getClassManagementId());
  }

  public static TeacherScheduleEvent mapToTeacherScheduleEvent(
      String classManagementToken,
      ClassManagement classManagement) {
    ClassMatching classMatching = classManagement.getClassMatching();
    Teacher teacher = classMatching.getTeacher();
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    Parents parents = applicationForm.getParents();

    return new TeacherScheduleEvent(
        parents.getPhoneNumber(),
        teacher.getTeacherInfo().getPhoneNumber(),
        classManagementToken);
  }

  public static TeacherNotifyClassInfoEvent mapToTeacherNotifyClassInfoEvent(
      String classManagementToken,
      String classNotifyToken,
      ClassManagement classManagement,
      List<MatchingTimetable> timetables) {
    ClassMatching classMatching = classManagement.getClassMatching();
    Teacher teacher = classMatching.getTeacher();
    ApplicationForm applicationForm = classMatching.getApplicationForm();
    Parents parents = applicationForm.getParents();

    List<DayTime> dayTimes =
        timetables.stream()
            .collect(
                Collectors.groupingBy(
                    MatchingTimetable::getDay,
                    Collectors.mapping(MatchingTimetable::getTimetableTime, Collectors.toList())))
            .entrySet()
            .stream()
            .map(entry -> new DayTime(entry.getKey(), entry.getValue()))
            .sorted(comparing(dayTime -> dayTime.getDay().getDayNum()))
            .toList();

    return new TeacherNotifyClassInfoEvent(
        applicationForm.getApplicationFormId(),
        applicationForm.getClassCount(),
        applicationForm.getClassTime(),
        // todo dayTime 공통모듈로 이동하고 vo 재사용해도될듯
        dayTimes.stream()
            .map(
                it ->
                    new TeacherNotifyClassInfoEvent.DayTime(it.getDay().toString(), it.getTimes()))
            .toList(),
        applicationForm.getAge(),
        applicationForm.getDistrict().getDescription(),
        applicationForm.getPay(),
        parents.getPhoneNumber(),
        teacher.getTeacherInfo().getPhoneNumber(),
        classNotifyToken,
        classManagementToken);
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
    return new ParentsExchangeEvent(
        teacher.getTeacherInfo().getNickName(),
        teacher.getTeacherInfo().getPhoneNumber(),
        parents.getPhoneNumber());
  }

  public static MatchingConfirmTeacherEvent mapToMatchingConfirmTeacherEvent(
      ClassManagement classManagement) {
    ClassGuideEvent classGuideEvent = mapToClassGuideEvent(classManagement);
    IntroduceFinishTalkEvent introduceFinishTalkEvent = mapToFinishTalkEvent(classManagement);
    IntroduceWriteFinishTalkEvent introduceWriteFinishTalkEvent =
        mapToWriteFinishTalkEvent(classManagement);
    return new MatchingConfirmTeacherEvent(
        classGuideEvent, introduceFinishTalkEvent, introduceWriteFinishTalkEvent);
  }

  private static ParentsClassNoticeEvent mapToParentsClassNoticeEvent(
      ClassManagement classManagement) {
    ApplicationForm applicationForm = classManagement.getClassMatching().getApplicationForm();
    Parents parents = applicationForm.getParents();
    return new ParentsClassNoticeEvent(parents.getPhoneNumber());
  }

  public static ClassGuideEvent mapToClassGuideEvent(ClassManagement classManagement) {
    ClassMatching classMatching = classManagement.getClassMatching();
    Teacher teacher = classMatching.getTeacher();
    return new ClassGuideEvent(teacher.getTeacherInfo().getPhoneNumber());
  }

  public static IntroduceFinishTalkEvent mapToFinishTalkEvent(ClassManagement classManagement) {
    ClassMatching classMatching = classManagement.getClassMatching();
    Teacher teacher = classMatching.getTeacher();
    return new IntroduceFinishTalkEvent(teacher.getTeacherInfo().getPhoneNumber());
  }

  public static IntroduceWriteFinishTalkEvent mapToWriteFinishTalkEvent(
      ClassManagement classManagement) {
    ApplicationForm applicationForm = classManagement.getClassMatching().getApplicationForm();
    List<ClassSchedule> schedules = classManagement.getSchedules();
    ClassSchedule classSchedule = schedules.get(0);
    Teacher teacher = classManagement.getClassMatching().getTeacher();
    return new IntroduceWriteFinishTalkEvent(
        applicationForm.getApplicationFormId(),
        classManagement.getSchedules().size(),
        classSchedule.getClassTime().getClassMinute(),
        teacher.getTeacherInfo().getPhoneNumber());
  }
}
