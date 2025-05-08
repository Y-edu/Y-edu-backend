package com.yedu.consumer.validator;

import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.common.event.bizppurio.ApplyAgreeEvent;
import com.yedu.common.event.bizppurio.InviteMatchingChannelInfoEvent;
import com.yedu.common.event.bizppurio.MatchingAcceptCaseInfoEvent;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.ClassGuideEvent;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceFinishTalkEvent;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent;
import com.yedu.common.event.bizppurio.MatchingRefuseCaseEvent;
import com.yedu.common.event.bizppurio.NotifyClassInfoEvent;
import com.yedu.common.event.bizppurio.PayNotificationEvent;
import com.yedu.common.event.bizppurio.PhotoHurryEvent;
import com.yedu.common.event.bizppurio.PhotoSubmitEvent;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
import com.yedu.common.event.bizppurio.TeacherAvailableTimeUpdateRequestEvent;
import com.yedu.common.event.bizppurio.TeacherClassRemindEvent;
import com.yedu.common.event.bizppurio.TeacherNotifyClassInfoEvent;
import com.yedu.common.event.bizppurio.TeacherNotifyClassInfoEvent.DayTime;
import com.yedu.common.event.bizppurio.TeacherScheduleEvent;
import jakarta.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class TemplateValidator {

  private final BizppurioMapper mapper;

  private final BizppurioApiTemplate apiTemplate;

  private static final String TESTER_PHONE_NUMBER = "01059367332";
  
  @PostConstruct
  void validate(){
    apiTemplate.send(mapper.mapToClassGuide(new ClassGuideEvent(TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToMatchingAcceptCase(new MatchingAcceptCaseInfoEvent("온라인","테스트","테스트","테스트","10",TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToApplyAgree(new ApplyAgreeEvent(TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToMatchingChannel(new InviteMatchingChannelInfoEvent("테스트","테스트",TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToPhotoHurry(new PhotoHurryEvent(TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToRefuseCase(new MatchingRefuseCaseEvent("테스트",TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToTeacherClassRemind(new TeacherClassRemindEvent("테스트",TESTER_PHONE_NUMBER,1)));
    apiTemplate.send(mapper.mapToIntroduceFinishTalk(new IntroduceFinishTalkEvent(TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToIntroduceWriteFinishTalk(new IntroduceWriteFinishTalkEvent("테스트",1,1,TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToTeacherAvailableTimeUpdateRequest(new TeacherAvailableTimeUpdateRequestEvent("테스트","1",TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToApplyPhotoSubmit(new PhotoSubmitEvent(TESTER_PHONE_NUMBER)));
    apiTemplate.send(mapper.mapToPayNotification(new PayNotificationEvent(TESTER_PHONE_NUMBER,"테스트",10000)));
    apiTemplate.send(mapper.mapToNotifyClass(new NotifyClassInfoEvent("테스트","테스트","테스트","테스트","테스트","테스트",1,TESTER_PHONE_NUMBER,"test")));
    apiTemplate.send(mapper.mapToTeacherSchedule(new TeacherScheduleEvent(TESTER_PHONE_NUMBER, TESTER_PHONE_NUMBER, "test")));
    apiTemplate.send(mapper.mapToTeacherNotifyClassInfo(new TeacherNotifyClassInfoEvent("테스트","2","50",
        List.of(new DayTime("월",List.of(LocalTime.now()))),"5","테스트",50000,TESTER_PHONE_NUMBER,TESTER_PHONE_NUMBER,"test","test")));
    apiTemplate.send(mapper.mapToRecommendTeacher(new RecommendTeacherEvent(TESTER_PHONE_NUMBER,"테스트","테스트","테스트",1, "test")));

  }

}
