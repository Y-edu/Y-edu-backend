package com.yedu.bizppurio.support.application.mapper;

import static com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.*;
import static com.yedu.common.event.bizppurio.MatchingParentsEvent.*;
import static com.yedu.common.event.bizppurio.ParentsClassInfoEvent.*;

import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.dto.req.ContentRequest;
import com.yedu.bizppurio.support.application.dto.req.content.*;
import com.yedu.common.event.bizppurio.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BizppurioMapper {
  @Value("${bizppurio.id}")
  private String id;

  @Value("${bizppurio.number}")
  private String number;

  @Value("${bizppurio.yedu_tutor}")
  private String yeduTutorKey;

  @Value("${bizppurio.yedu_apply}")
  private String yeduApplyKey;

  @Value("${bizppurio.yedu_matching}")
  private String yeduMatchingKey;

  @Value("${bizppurio.yedu_official}")
  private String yeduOfficialKey;

  @Value("${bizppurio.yedu_apply_template.agree}")
  private String applyAgree;

  @Value("${bizppurio.yedu_apply_template.photo_hurry}")
  private String applyPhotoHurry;

  @Value("${bizppurio.yedu_apply_template.photo_submit}")
  private String applyPhotoSubmit;

  @Value("${bizppurio.yedu_matching_template.notify_class}")
  private String notifyClass;

  @Value("${bizppurio.yedu_matching_template.accept_case}")
  private String matchingAcceptCase;

  @Value("${bizppurio.yedu_matching_template.refuse_case}")
  private String matchingRefuseCase;

  @Value("${bizppurio.yedu_matching_template.refuse_case_now}")
  private String matchingRefuseCaseNow;

  @Value("${bizppurio.yedu_matching_template.refuse_case_district}")
  private String matchingRefuseCaseDistrict;

  @Value("${bizppurio.yedu_matching_template.matching_channel}")
  private String matchingChannel;

  @Value("${bizppurio.yedu_offical_template.recommend_guide}")
  private String recommendGuid;

  @Value("${bizppurio.yedu_offical_template.recommend_teacher}")
  private String recommendTeacher;

  @Value("${bizppurio.yedu_offical_template.notify_calling}")
  private String notifyCalling;

  @Value("${bizppurio.yedu_offical_template.parents_exchange}")
  private String parentsExchange;

  @Value("${bizppurio.yedu_offical_template.parents_class_notice}")
  private String parentsClassNotice;

  @Value("${bizppurio.yedu_offical_template.parents_class_info}")
  private String parentsClassInfo;

  @Value("${bizppurio.yedu_offical_template.teacher_class_notify_info}")
  private String teacherClassNotifyInfo;

  @Value("${bizppurio.yedu_offical_template.teacher_schedule}")
  private String teacherSchedule;

  @Value("${bizppurio.yedu_offical_template.teacher_setting}")
  private String teacherSetting;

  @Value("${bizppurio.yedu_offical_template.teacher_class_remind}")
  private String teacherClassRemind;

  @Value("${bizppurio.yedu_offical_template.class_guide}")
  private String classGuide;

  @Value("${bizppurio.yedu_offical_template.introduce_finish_talk}")
  private String introduceFinishTalk;

  @Value("${bizppurio.yedu_offical_template.introduce_write_finish_talk}")
  private String introduceWriteFinishTalk;

  @Value("${bizppurio.yedu_offical_template.pay_notification}")
  private String payNotification;

  @Value("${bizppurio.url.apply_agree}")
  private String applyAgreeUrl;

  @Value("${bizppurio.url.photo_submit}")
  private String photoSubmitUrl;

  @Value("${bizppurio.url.photo_hurry}")
  private String photoHurryUrl;

  @Value("${bizppurio.url.refuse_change_form}")
  private String refuseChangeFormUrl;

  @Value("${bizppurio.url.result_share_form}")
  private String resultShareFormUrl;

  @Value("${bizppurio.url.class_guide}")
  private String classGuideUrl;

  private static final String WEB_LINK = "WL";
  private static final String BOT = "BK";
  private static final String CHANNEL = "AC";
  private static final String MESSAGE = "MD";

  public CommonRequest mapToApplyAgree(ApplyAgreeEvent applyAgreeEvent) {
    String message =
        ("이제 한 단계만 남았어요! \n"
            + "\n"
            + "선생님께 과외를 찾아드리고, 보수를 정산드리기 위해 위임 약관 동의가 필요합니다. \uD83D\uDE00\n"
            + "\n"
            + "아래 서류를 미리 준비해주세요\n"
            + "1. 신분증 사본 (원천세 신고용)\n"
            + "2. 재학/졸업 증명서 or 학생증\n"
            + "\n"
            + "아래 약관 동의 후엔, 선생님 등록이 완료되어 ‘매칭 카카오톡 채널’에서 과외건을 받아보실 수 있습니다. \uD83D\uDE04");
    CommonButton webLinkButton =
        new WebButton("약관 동의하고 등록완료하기", WEB_LINK, applyAgreeUrl, applyAgreeUrl);
    Message messageBody =
        new ButtonMessage(message, yeduApplyKey, applyAgree, new CommonButton[] {webLinkButton});
    return createCommonRequest(messageBody, applyAgreeEvent.phoneNumber());
  }

  public CommonRequest mapToApplyPhotoSubmit(PhotoSubmitEvent photoSubmitEvent) {
    String message =
        ("안녕하세요 선생님 \uD83D\uDE42\n"
            + "\n"
            + "지원서가 정상적으로 제출되었어요.\n"
            + "Y-Edu의 선생님으로 지원해주셔서 감사합니다. \n"
            + "\n"
            + "지원서는 Y-Edu가 프로필로 만들어, 과외 매칭 시 학부모님께 전달드릴게요. \n"
            + "\n"
            + "다음 단계로 프로필 사진과 영상을 구글폼으로 제출해주세요. \uD83D\uDE00\n"
            + "\n"
            + "✅ 제출 포인트\n"
            + "- [필수] 프로필 사진 \n"
            + "- [선택] 영어 수업 소개영상 \n"
            + "\n"
            + "구체적인 설명은 아래 버튼을 눌러 구글폼에서 확인할 수 있으며, 꼭 3일 이내 제출 부탁드려요 \uD83D\uDE47\uD83C\uDFFB\u200D♀\uFE0F");
    CommonButton webLinkButton =
        new WebButton("사진/영상 제출하기", WEB_LINK, photoSubmitUrl, photoSubmitUrl);
    Message messageBody =
        new ButtonMessage(
            message, yeduApplyKey, applyPhotoSubmit, new CommonButton[] {webLinkButton});
    return createCommonRequest(messageBody, photoSubmitEvent.phoneNumber());
  }

  public CommonRequest mapToPhotoHurry(PhotoHurryEvent photoHurryEvent) {
    String message =
        ("[D-1] 사진 & 영상 제출 리마인드\n"
            + "\n"
            + "잊지말고 내일까지 아래 링크를 통해 소개 사진 및 영상 제출(선택)을 제출해 주세요. \n"
            + "\n"
            + "지금 당장 좋은 사진이 없다면 가장 깔끔한 사진으로 제출하고, 이후 수정해주셔도 괜찮습니다. \n"
            + "\n"
            + "선생님과 좋은 인연으로 만날 수 있길 기대할게요.\uD83D\uDE42");
    CommonButton webLinkButton =
        new WebButton("사진/영상 제출하기", WEB_LINK, photoHurryUrl, photoHurryUrl);
    Message messageBody =
        new ButtonMessage(
            message, yeduApplyKey, applyPhotoHurry, new CommonButton[] {webLinkButton});
    return createCommonRequest(messageBody, photoHurryEvent.phoneNumber());
  }

  public CommonRequest mapToNotifyClass(NotifyClassInfoEvent notifyClassInfoEvent) {
    String message;
    if (notifyClassInfoEvent.online().equals("비대면")) {
      message =
          ("["
              + notifyClassInfoEvent.applicationFormId()
              + " 과외건 공지]\n"
              + "\n"
              + "안녕하세요 "
              + notifyClassInfoEvent.nickName()
              + "선생님!\n"
              + "현재 "
              + notifyClassInfoEvent.classType()
              + " "
              + notifyClassInfoEvent.district()
              + "에 과외건이 들어와 공지드립니다. \uD83D\uDD14\n"
              + "\n"
              + "아래 버튼을 통해 과외건 정보를 확인하고, '신청하기' / '넘기기' 중 하나를 3시간 안에 버튼을 눌러 선택해주세요.\n"
              + "\n"
              + "* 무응답이 반복되면, 과외공지에 전송이 줄어들 수 있습니다.\n"
              + "* '넘기기'를 연속으로 한다고 하여 받는 불이익은 없습니다.\n"
              + "\n"
              + "\uD83E\uDD1E\uD83C\uDFFB신청 시, 철회는 불가합니다! 반드시 수업 시간과 장소를 확인 후 가능한 수업을 신청해주세요");
    } else {
      message =
          ("["
              + notifyClassInfoEvent.applicationFormId()
              + " 과외건 공지]\n"
              + "\n"
              + "안녕하세요 "
              + notifyClassInfoEvent.nickName()
              + "선생님!\n"
              + "현재 "
              + notifyClassInfoEvent.classType()
              + " "
              + notifyClassInfoEvent.district()
              + " "
              + notifyClassInfoEvent.dong()
              + "에 과외건이 들어와 공지드립니다. \uD83D\uDD14\n"
              + "\n"
              + "아래 버튼을 통해 과외건 정보를 확인하고, '신청하기' / '넘기기' 중 하나를 3시간 안에 버튼을 눌러 선택해주세요.\n"
              + "\n"
              + "* 무응답이 반복되면, 과외공지에 전송이 줄어들 수 있습니다.\n"
              + "* '넘기기'를 연속으로 한다고 하여 받는 불이익은 없습니다.\n"
              + "\n"
              + "\uD83E\uDD1E\uD83C\uDFFB신청 시, 철회는 불가합니다! 반드시 수업 시간과 장소를 확인 후 가능한 수업을 신청해주세요");
    }
    String classUrl = "https://www.yedu-tutor.com/teacher/notify/" + notifyClassInfoEvent.token();
    CommonButton webButton = new WebButton("과외 정보 확인하기", WEB_LINK, classUrl, classUrl);
    Message messageBody =
        new ButtonMessage(message, yeduMatchingKey, notifyClass, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, notifyClassInfoEvent.phoneNumber());
  }

  public CommonRequest mapToMatchingAcceptCase(
      MatchingAcceptCaseInfoEvent matchingAcceptCaseInfoEvent) {
    String message;
    if (matchingAcceptCaseInfoEvent.online().equals("비대면")) {
      message =
          ("[과외 신청완료]"
              + "\n"
              + "\n"
              + matchingAcceptCaseInfoEvent.district()
              + " "
              + matchingAcceptCaseInfoEvent.classType()
              + " "
              + matchingAcceptCaseInfoEvent.age()
              + " 과외건 신청이 완료되었습니다! \uD83D\uDE42\n"
              + "\n"
              + "학부모님이 신청해주신 선생님 프로필들을 전달 받으신 후, 답변 주실 예정입니다.\n"
              + "\n"
              + "학부모님께 답변이 오면 매칭 성사 여부를 3일 이내에 공유드릴게요.\n"
              + "\n"
              + "3일 이내 매칭이 진행되지 않는다면, 여러 사유에 의한 미진행으로 생각해주시면 좋을 것 같습니다! \uD83D\uDE4F");
    } else {
      message =
          ("[과외 신청완료]"
              + "\n"
              + "\n"
              + matchingAcceptCaseInfoEvent.district()
              + " "
              + matchingAcceptCaseInfoEvent.dong()
              + " "
              + matchingAcceptCaseInfoEvent.classType()
              + " "
              + matchingAcceptCaseInfoEvent.age()
              + " 과외건 신청이 완료되었습니다! \uD83D\uDE42\n"
              + "\n"
              + "학부모님이 신청해주신 선생님 프로필들을 전달 받으신 후, 답변 주실 예정입니다.\n"
              + "\n"
              + "학부모님께 답변이 오면 매칭 성사 여부를 3일 이내에 공유드릴게요.\n"
              + "\n"
              + "3일 이내 매칭이 진행되지 않는다면, 여러 사유에 의한 미진행으로 생각해주시면 좋을 것 같습니다! \uD83D\uDE4F");
    }
    Message messageBody = new TextMessage(message, yeduMatchingKey, matchingAcceptCase);
    return createCommonRequest(messageBody, matchingAcceptCaseInfoEvent.phoneNumber());
  }

  public CommonRequest mapToRefuseCase(MatchingRefuseCaseEvent matchingRefuseCaseEvent) {
    String message =
        ("[수업 요건 변경 안내]\n"
            + "\n"
            + matchingRefuseCaseEvent.nickName()
            + " 선생님. \n"
            + "\n"
            + "가능하지 않은 과외 공지가 자꾸 발송된다면 수업 요건 업데이트를 부탁드립니다.\n"
            + "\n"
            + "가능하신 일정과 지역에 변동 사항을 현재 가능한 요건으로 수정하시면 더 필요한 과외건 공지를 받아보실 수 있습니다. \n"
            + "\n"
            + "요건 수정이나, 과외 공지 중단 요청은 상담 직원에게 말씀해주세요.");
    CommonButton simpleButton = new SimpleButton("상담 매니저에게 요청하기", BOT);
    Message messageBody =
        new ButtonMessage(
            message, yeduMatchingKey, matchingRefuseCase, new CommonButton[] {simpleButton});
    return createCommonRequest(messageBody, matchingRefuseCaseEvent.phoneNumber());
  }

  public CommonRequest mapToRefuseCaseNow(MatchingRefuseCaseNowEvent matchingRefuseCaseEvent) {
    String message =
        ("[과외건 공지 수신 설정 안내]\n"
            + "\n"
            + matchingRefuseCaseEvent.nickName()
            + " 선생님.\n"
            + "\n"
            + "’지금은 수업이 불가’한 이유로 매칭을 거절하셨네요!\n"
            + "\n"
            + "혹시 현재 과외가 어려운 상황으로 잠시 과외건 공지를 받지 않으시려면 아래 버튼을 클릭하여 과외 설정 페이지 접속 후\n"
            + "\n"
            + "‘과외건 공지 받기’ 메뉴에서 비활성화 하시면 메세지 전송이 중단됩니다. \uD83D\uDE42");
    CommonButton webButton =
        new WebButton("과외 설정 페이지로 이동", WEB_LINK, refuseChangeFormUrl, refuseChangeFormUrl);
    Message messageBody =
        new ButtonMessage(
            message, yeduMatchingKey, matchingRefuseCaseNow, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, matchingRefuseCaseEvent.phoneNumber());
  }

  public CommonRequest mapToRefuseCaseDistrict(
      MatchingRefuseCaseDistrictEvent matchingRefuseCaseEvent) {
    String message =
        ("[과외 가능지역 변경 안내]\n"
            + "\n"
            + matchingRefuseCaseEvent.nickName()
            + " 선생님.\n"
            + "\n"
            + "현재 발송되고 있는 과외건 공지가 실제 가능한 지역과 다를 경우, 과외 가능 지역 업데이트가 필요합니다.\n"
            + "\n"
            + "아래 버튼을 클릭하여 과외 설정 페이지 접속 후 지역을 변경하실 수 있습니다.\n"
            + "\n"
            + "과외 설정을 최신화 하시면, 가능한 지역의 과외건 공지만 받아보실 수 있습니다. \uD83D\uDE09");
    CommonButton webButton =
        new WebButton("과외 설정 페이지로 이동", WEB_LINK, refuseChangeFormUrl, refuseChangeFormUrl);
    Message messageBody =
        new ButtonMessage(
            message, yeduMatchingKey, matchingRefuseCaseDistrict, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, matchingRefuseCaseEvent.phoneNumber());
  }

  public CommonRequest mapToMatchingChannel(
      InviteMatchingChannelInfoEvent inviteMatchingChannelInfo) {
    String message =
        ("[매칭 알림톡 초대] \n"
            + "\n"
            + "이름 : "
            + inviteMatchingChannelInfo.name()
            + "\n"
            + "영어이름 : "
            + inviteMatchingChannelInfo.nickName()
            + "\n"
            + "전화번호 : "
            + inviteMatchingChannelInfo.phoneNumber()
            + "\n"
            + "\n"
            + inviteMatchingChannelInfo.nickName()
            + " 선생님 등록을 위한 모든 절차가 완료되었습니다. \uD83C\uDF89\n"
            + "\n"
            + "해당 채널은 선생님이 신청하셨던 지역의 ‘과외 공지’가 전달되는 채널입니다.  \n"
            + "\n"
            + "이 채널만큼은 꼼꼼히 확인 및 반응해주셔야 해요!\n"
            + "\n"
            + "공지되는 과외건에 대해 3시간 이내에 반복적으로 반응이 없을 경우, 과외건 공지가 줄어들게 된다는 사실을 명심해주세요! ☝\uD83C\uDFFB\n"
            + "\n"
            + "앞으로 잘부탁드립니다 선생님! \uD83D\uDE42");
    CommonButton simpleButton = new SimpleButton("채널 추가", CHANNEL);
    Message messageBody =
        new ButtonMessage(
            message, yeduMatchingKey, matchingChannel, new CommonButton[] {simpleButton});
    return createCommonRequest(messageBody, inviteMatchingChannelInfo.phoneNumber());
  }

  public CommonRequest mapToRecommendGuid(RecommendGuideEvent recommendGuideEvent) {
    String message =
        ("[선생님 프로필을 둘러보셨다면] ☝\uD83C\uDFFB\n"
            + "\n"
            + "원하시는 선생님 메세지에 ‘이 선생님과 수업할래요’ 버튼을 눌러주세요! \n"
            + "\n"
            + "혹시 선택 전, 문의사항이 있으시다면 편하게 물어봐주시면 됩니다. \uD83D\uDE42\n"
            + "\n"
            + "Y-English는 시범과외는 지원하지 않지만 첫 수업 후, 환불이 가능하기에 부담 없이 수업을 진행해 보실 수 있습니다. \uD83D\uDE00\n"
            + "\n"
            + "내일까지 둘러보신 후, 희망 선생님을 알려주세요");
    CommonButton simpleButton = new SimpleButton("매칭 담당자에게 문의하기", BOT);
    Message messageBody =
        new ButtonMessage(
            message, yeduOfficialKey, recommendGuid, new CommonButton[] {simpleButton});
    return createCommonRequest(messageBody, recommendGuideEvent.phoneNumber());
  }

  public CommonRequest mapToRecommendTeacher(RecommendTeacherEvent recommendTeacherEvent) {
    String title =
        "추천 : #{name} 선생님".strip().replace("#{name}", recommendTeacherEvent.teacherNickName());

    String message =
        """
신청해주신 #{district} 과외 매칭을 위한 선생님을 안내드립니다.

☀️#{name}☀️을 아이의 #{subject}을 책임지고 지도해줄 선생님으로 추천드려요!

Y-Edu가 상담 내용과 신청서를 꼼꼼히 살펴보고 추천드리는 선생님이에요. 😀

아래 버튼을 눌러 '상세프로필'을 천천히 살펴보시고 매칭 희망하시는 경우 버튼을 눌러 제출해주세요
         """
            .strip()
            .replace("#{name}", recommendTeacherEvent.teacherNickName())
            .replace("#{district}", recommendTeacherEvent.district())
            .replace("#{subject}", recommendTeacherEvent.classType());
    String teacherUrl =
        "https://www.yedu-tutor.com/teacher/recommend/#{token}?subject=#{subject}"
            .replace("#{token}", recommendTeacherEvent.token())
            .replace("#{subject}", recommendTeacherEvent.classType());

    CommonButton webButton = new WebButton("선생님 프로필 확인하기", WEB_LINK, teacherUrl, teacherUrl);
    Message messageBody =
        new EmphasizeButtonMessage(
            message, title, yeduOfficialKey, recommendTeacher, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, recommendTeacherEvent.parentsPhoneNumber());
  }

  public CommonRequest mapToNotifyCalling(NotifyCallingEvent notifyCallingEvent) {
    String message =
        ("신청서 접수 완료! ✅\n"
            + "\n"
            + "매칭 매니저가 꼼꼼하게 확인하고 있어요. 48시간 이내로 전화드릴게요. \n"
            + "\n"
            + "구체적인 수업 방향성과 교재는 선생님 매칭 후 상담하시게 됩니다. \n"
            + "\n"
            + "매칭 매니저와 전화상담에서는 아이에게 딱 맞는 선생님에 대해 상담과 추천을 받을 수 있어요\uD83D\uDE42");
    Message messageBody = new TextMessage(message, yeduOfficialKey, notifyCalling);
    return createCommonRequest(messageBody, notifyCallingEvent.phoneNumber());
  }

  public CommonRequest mapToParentsExchangePhoneNumber(ParentsExchangeEvent parentsExchangeEvent) {
    String message =
        ("\uD83C\uDF89과외 매칭이 성사됐어요! \uD83C\uDF89\n"
            + "\n"
            + "✅ "
            + parentsExchangeEvent.nickName()
            + " 선생님 연락처 \n"
            + ": "
            + parentsExchangeEvent.teacherPhoneNumber()
            + "\n"
            + "\n"
            + "선생님이 24시간 내로 전화상담을 주실 예정이며, 학부모님께서 먼저 연락 남기셔도 괜찮습니다 \uD83D\uDE42\n"
            + "\n"
            + "Y-Edu는 관리형 매칭 서비스로, 이후 수업료 입금은 선생님이 아닌 Y-Edu를 통해 진행됩니다. \n"
            + "\n"
            + "또한, 선생님 교체를 원하실 경우 언제든지 말씀해주시면 빠르게 반영하여 교체 진행해 드릴게요 \uD83D\uDE0A\n"
            + " \n"
            + "문의사항이 있으신 경우 언제든 본 채팅방을 통해 남겨주시기 바랍니다.\n"
            + "\n"
            + "감사합니다!");
    Message messageBody = new TextMessage(message, yeduOfficialKey, parentsExchange);
    return createCommonRequest(messageBody, parentsExchangeEvent.parentsPhoneNumber());
  }

  public CommonRequest mapToParentsClassNotice(ParentsClassNoticeEvent parentsClassNoticeEvent) {
    String message =
        ("\uD83D\uDCE2 수업 진행 시 주의사항을 안내드려요 \n"
            + "\n"
            + "✅ 수업 취소(휴강) 규정\n"
            + "수업 취소(휴강) 요청은 최소 ‘전날 밤 10시'까지 부탁드립니다.\n"
            + "\n"
            + "전날 밤 10시 이후, 혹은 수업 당일에 갑작스럽게 수업을 취소(휴강) 하시는 경우, 회차가 진행된 것으로 인정되어 수업료가 차감됩니다. \n"
            + "\n"
            + "선생님들께서 수업을 위해 대중교통으로 이동하는 도중 수업이 취소되거나 시간대 변경 연락을 갑작스럽게 받으시는 경우가 빈번하여,\n"
            + "휴강 관련 규정을 마련하였으니 선생님들을 위해 양해 부탁드려요 \uD83D\uDE4F\n"
            + "\n"
            + "감사합니다! \uD83D\uDE42");
    Message messageBody = new TextMessage(message, yeduOfficialKey, parentsClassNotice);
    return createCommonRequest(messageBody, parentsClassNoticeEvent.phoneNumber());
  }

  public CommonRequest mapToParentsClassInfo(ParentsClassInfoEvent parentsClassInfoEvent) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    List<ClassTime> classTimes = parentsClassInfoEvent.classTimes();
    List<Integer> classMinutes =
        classTimes.stream().map(ClassTime::classMinute).distinct().sorted().toList();
    AtomicInteger index = new AtomicInteger();
    String classMinuteString =
        classMinutes.stream()
            .map(minute -> minute + (index.incrementAndGet() == classMinutes.size() ? "" : "분"))
            .collect(Collectors.joining(", "));

    String classTimeString =
        classTimes.stream()
            .map(
                classTime ->
                    String.format(
                        "[ %s %s 부터 %d분 ]",
                        classTime.day(),
                        classTime.startTime().format(formatter),
                        classTime.classMinute()))
            .collect(Collectors.joining("\n"));

    FirstDay firstDay = parentsClassInfoEvent.firstDay();
    String month = String.valueOf(firstDay.date().getMonthValue());
    String day = String.valueOf(firstDay.date().getDayOfMonth());
    DayOfWeek dayOfWeek = firstDay.date().getDayOfWeek();
    String firstDayOfWeek = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN); // "목"

    String message =
        ("\uD83D\uDCCB 전화상담 후 수업정보 전달 \uD83D\uDCCB\n"
            + "\n"
            + parentsClassInfoEvent.nickName()
            + " 선생님\n"
            + "✅ 수업 시수 : 주 "
            + classTimes.size()
            + "회 "
            + classMinuteString
            + "분 \n"
            + "✅ 수업 시간 : \n"
            + classTimeString
            + "\n"
            + "✅ 첫 수업 : "
            + month
            + "월 "
            + day
            + "일 "
            + firstDayOfWeek
            + "요일 "
            + firstDay.start().format(formatter)
            + "\n"
            + "✅ 교재 : "
            + parentsClassInfoEvent.book()
            + "\n"
            + "\n"
            + "선생님과 전화 상담 시 확정한 수업 정보를 정리드릴게요. \n"
            + "\n"
            + "앞으로 잘 부탁드립니다 \uD83D\uDE47\uD83C\uDFFB\u200D♀\uFE0F");
    Message messageBody = new TextMessage(message, yeduOfficialKey, parentsClassInfo);
    return createCommonRequest(messageBody, parentsClassInfoEvent.parentsPhoneNumber());
  }

  public CommonRequest mapToTeacherNotifyClassInfo(TeacherExchangeEvent teacherExchangeEvent) {
    String message =
        """
🎉 과외 매칭 성사를 축하드립니다!

#{applicationFormId}
✅ 수업 시수 : 주 #{count}회 #{time}분
✅ 정규 수업 요일, 일시
#{dayTimes}
✅ 아이 나이 : #{age}
✅ 장소 : #{district}
✅ 보수 : #{pay}원

자세한 수업 정보는
아래의 버튼을 눌러 확인해주세요
       """
            .strip()
            .replace("#{applicationFormId}", teacherExchangeEvent.applicationFormId())
            .replace("#{count}", teacherExchangeEvent.classCount())
            .replace("#{time}", teacherExchangeEvent.time())
            .replace(
                "#{dayTimes}",
                teacherExchangeEvent.dayTimes().stream()
                    .map(
                        dayTime ->
                            dayTime.day()
                                + " "
                                + dayTime.times().stream()
                                    .map(LocalTime::toString)
                                    .collect(Collectors.joining(", ")))
                    .collect(Collectors.joining("\n"))
                    .replace("#{age}", teacherExchangeEvent.age())
                    .replace("#{district}", teacherExchangeEvent.district())
                    .replace("#{pay}", String.valueOf(teacherExchangeEvent.money())));

    CommonButton webButton =
        new WebButton(
            "수업 정보 확인",
            WEB_LINK,
            "https://www.yedu-tutor.com/teacher/match-success/"
                + teacherExchangeEvent.classNotifyToken(),
            "https://www.yedu-tutor.com/teacher/match-success/"
                + teacherExchangeEvent.classNotifyToken());
    Message messageBody =
        new ButtonMessage(
            message, yeduMatchingKey, teacherClassNotifyInfo, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, teacherExchangeEvent.teacherPhoneNumber());
  }

  public CommonRequest mapToTeacherSchedule(TeacherExchangeEvent teacherExchangeEvent) {
    String message =
        """
📌 학부모님 연락처 : #{phoneNumer}
바로 학부모님께 문자를 통해 선생님을소개한 후, 전화상담 시간을 잡아주세요

📌 24시간 내로 전화상담을 진행하며아래 내용을 확정해주세요
🌟 수업 방향성
🌟 수업 교재
🌟 첫 수업 날짜
🌟 정규 수업 요일, 일시
🌟 교재명

📌 전화 후 반드시!
아래 버튼을 눌러 확정된 상담 결과를전달해주세요
       """
            .strip()
            .replace("#{phoneNumer}", teacherExchangeEvent.parentsPhoneNumber());

    CommonButton webButton =
        new WebButton(
            "상담 결과 전달",
            WEB_LINK,
            "https://yedu-tutor.com/result/" + teacherExchangeEvent.classManagementToken(),
            "https://xyedu-tutor.com/result/" + teacherExchangeEvent.classManagementToken());
    Message messageBody =
        new ButtonMessage(
            message, yeduMatchingKey, teacherSchedule, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, teacherExchangeEvent.teacherPhoneNumber());
  }

  public CommonRequest mapToTeacherAvailableTimeUpdateRequest(
      TeacherAvailableTimeUpdateRequestEvent event) {
    String message =
        """
📣 #{닉네임} 선생님, 수업 가능시간을 알려주세요.

앞으로 Y-Edu에서 수업 가능 시간을 고려하여 과외 공지를 전달드리려 해요.

이전 활동성 조사에 답변 주셨던 분들을 대상으로, 수업 가능 시간 설정을 요청드리고 있습니다.

미 설정 시, 과외 공지 전달에 지장이 있을 수 있으니, 꼭 빠르게 아래 링크로 설정해 주세요 🙂
       """
            .strip()
            .replace("#{닉네임}", event.name());

    CommonButton webButton =
        new WebButton(
            "수업 가능시간 설정하기",
            WEB_LINK,
            "https://yedu-tutor.com/teachersetting/time?token=" + event.token(),
            "https://yedu-tutor.com/teachersetting/time?token=" + event.token());
    Message messageBody =
        new ButtonMessage(message, yeduOfficialKey, teacherSetting, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, event.teacherPhoneNumber());
  }

  public CommonRequest mapToTeacherClassRemind(TeacherClassRemindEvent teacherClassRemindEvent) {
    String message =
        ("⏰ 상담 내용 공유 리마인드\n"
            + "\n"
            + "안녕하세요, "
            + teacherClassRemindEvent.nickName()
            + "선생님!\n"
            + "\n"
            + "매칭 후 24시간 이내에 전화 상담 결과를 공유해주시지 않아 연락드려요.\n"
            + "\n"
            + "정확한 보수 정산을 위해 상담 결과 공유가 꼭 필요합니다! 첫 수업일 전에 공유 부탁드려요 \uD83D\uDE4F");
    CommonButton webButton =
        new WebButton(
            "전화상담 결과 공유하기",
            WEB_LINK,
            resultShareFormUrl + teacherClassRemindEvent.managementId(),
            resultShareFormUrl + teacherClassRemindEvent.managementId());
    Message messageBody =
        new ButtonMessage(
            message, yeduMatchingKey, teacherClassRemind, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, teacherClassRemindEvent.phoneNumber());
  }

  public CommonRequest mapToClassGuide(ClassGuideEvent classGuideEvent) {
    String message =
        ("\uD83D\uDCE2 [필독] 활동 규정 \uD83D\uDCE2\n"
            + "\n"
            + "과외 진행 시 꼭 확인이 필요한 규정을 안내드려요. \n"
            + "\n"
            + "첫 수업일 전, 반드시 아래 링크를 통해 규정집을 확인해주세요 \uD83D\uDE4F");
    CommonButton webButton = new WebButton("수업 가이드 보기", WEB_LINK, classGuideUrl, classGuideUrl);
    Message messageBody =
        new ButtonMessage(message, yeduMatchingKey, classGuide, new CommonButton[] {webButton});
    return createCommonRequest(messageBody, classGuideEvent.phoneNumber());
  }

  public CommonRequest mapToIntroduceFinishTalk(IntroduceFinishTalkEvent introduceFinishTalkEvent) {
    String message =
        ("\uD83D\uDCE2 완료톡 쌓기 안내 \uD83D\uDCE2\n"
            + "\n"
            + "매 수업이 끝난 직후, 본 채널에 ‘수업을 완료했다’는 기록을 메세지로 보내주셔야 합니다! ☝\uD83C\uDFFB\n"
            + "\n"
            + "다음 전송한 메세지 내용을 복사하신 후, 아래에 수업 완료 내역을 추가하여 다시 보내주세요. \uD83D\uDE42\n"
            + "\n"
            + "완료톡에 기반해 급여 정산이 이루어지기에 정확하게 작성 부탁드립니다. \n"
            + "\n"
            + "1\uFE0F⃣ 실제로 진행된 수업 분을 써주세요. \n"
            + "(지각해 40분 수업했다면 40분 기록)\n"
            + "2\uFE0F⃣ 휴강도 적어주세요. \n"
            + "3\uFE0F⃣ 담당자가 완료톡을 수정하여 보냈다면 수정한 메세지를 복붙해 이어 추가해주세요.");
    Message messageBody = new TextMessage(message, yeduTutorKey, introduceFinishTalk);
    return createCommonRequest(messageBody, introduceFinishTalkEvent.phoneNumber());
  }

  public CommonRequest mapToIntroduceWriteFinishTalk(
      IntroduceWriteFinishTalkEvent introduceWriteFinishTalkEvent) {
    String message =
        ("\uD83D\uDCCC 완료톡 작성 예시 \uD83D\uDCCC\n"
            + "\n"
            + "완료톡 작성 예시를 알려드릴게요. 매 수업이 끝난 후 아래 내용대로 완료톡 작성을 부탁드립니다! \uD83D\uDE0A\n"
            + "\n"
            + "예시)\n"
            + introduceWriteFinishTalkEvent.applicationFormId()
            + " (주 "
            + introduceWriteFinishTalkEvent.count()
            + "회 "
            + introduceWriteFinishTalkEvent.time()
            + "분) ("
            + (introduceWriteFinishTalkEvent.count() * 4)
            + "회 기준)\n"
            + "- 0월 00일 1회차 00분 완료 \n"
            + "\n"
            + "0을 실제 숫자로 채워주세요. 2회차는 진행 후, 1회차 내용 아래에 추가하여 보내주시면 됩니다.");
    Message messageBody = new TextMessage(message, yeduTutorKey, introduceWriteFinishTalk);
    return createCommonRequest(messageBody, introduceWriteFinishTalkEvent.phoneNumber());
  }

  public CommonRequest mapToPayNotification(PayNotificationEvent event) {
    String message =
        """
        어머님 안녕하세요 😊
        #{name} 선생님과 매칭이 완료되어수업료 안내드립니다.

        💶 수업료: #{pay}만원
        🌟 입금계좌: 신한 110-149-528751조현숙 (YEdu)
        👦🏻 입금자명: 어머님 전화번호 뒷자리4자리로 기입 부탁드립니다

        입금이 확인되면 선생님께서 구체적인수업 일정 관련해 연락드릴 예정입니다.

        문의사항이 있으신 경우 언제든 본 채팅방을 통해 남겨주시기 바랍니다.
        감사합니다!
        """
            .strip()
            .replace("#{name}", event.nickName())
            .replace("#{pay}", String.valueOf(event.pay() / 10_000));

    Message messageBody = new TextMessage(message, yeduOfficialKey, payNotification);
    return createCommonRequest(messageBody, event.parentPhoneNumber());
  }

  private CommonRequest createCommonRequest(Message messageBody, String phoneNumber) {
    String refKey = UUID.randomUUID().toString().replace("-", "");
    ContentRequest contentRequest = new ContentRequest(messageBody);
    return new CommonRequest(id, "at", number, phoneNumber, contentRequest, refKey);
  }
}
