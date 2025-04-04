package com.yedu.backend.bizppurio.application.mapper;

import com.yedu.backend.bizppurio.application.dto.req.content.*;
import com.yedu.backend.domain.parents.domain.entity.constant.Online;
import com.yedu.backend.bizppurio.application.dto.req.CommonRequest;
import com.yedu.backend.bizppurio.application.dto.req.ContentRequest;
import com.yedu.backend.global.event.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BizppurioMapper {
    @Value("${bizppurio.id}")
    private String id;
    @Value("${bizppurio.number}")
    private String number;
    @Value("${bizppurio.phone_number}")
    private String phoneNumber;
    @Value("${bizppurio.yedu_tutor}")
    private String yeduTutorKey;
    @Value("${bizppurio.yedu_apply}")
    private String yeduApplyKey;
    @Value("${bizppurio.yedu_matching}")
    private String yeduMatchingKey;
    @Value("${bizppurio.yedu_official}")
    private String yeduOfficialKey;
    @Value("${bizppurio.yedu_tutor_template.apply_channel}")
    private String applyChannel;
    @Value("${bizppurio.yedu_apply_template.agree}")
    private String applyAgree;
    @Value("${bizppurio.yedu_apply_template.photo_hurry}")
    private String applyPhotoHurry;
    @Value("${bizppurio.yedu_apply_template.photo_submit}")
    private String applyPhotoSubmit;
    @Value("${bizppurio.yedu_apply_template.counsel_start}")
    private String counselStart;
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
    @Value("${bizppurio.yedu_offical_template.before_check}")
    private String beforeCheck;
    @Value("${bizppurio.yedu_offical_template.write_application_form}")
    private String writeApplicationForm;
    @Value("${bizppurio.yedu_offical_template.recommend_teacher}")
    private String recommendTeacher;
    @Value("${bizppurio.yedu_offical_template.notify_calling}")
    private String notifyCalling;
    @Value("${bizppurio.url.apply_agree}")
    private String applyAgreeUrl;
    @Value("${bizppurio.url.photo_submit}")
    private String photoSubmitUrl;
    @Value("${bizppurio.url.photo_hurry}")
    private String photoHurryUrl;
    @Value("${bizppurio.url.write_application_form}")
    private String writeApplicationFormUrl;
    @Value("${bizppurio.url.refuse_change_form}")
    private String refuseChangeFormUrl;

    private static final String WEB_LINK = "WL";
    private static final String BOT = "BK";
    private static final String CHANNEL = "AC";
    private static final String MESSAGE = "MD";

    public CommonRequest mapToApplyAgree(ApplyAgreeEvent applyAgreeEvent) {
        String message = ("이제 한 단계만 남았어요! \n" +
                "\n" +
                "선생님께 과외를 찾아드리고, 보수를 정산드리기 위해 위임 약관 동의가 필요합니다. \uD83D\uDE00\n" +
                "\n" +
                "아래 서류를 미리 준비해주세요\n" +
                "1. 신분증 사본 (원천세 신고용)\n" +
                "2. 재학/졸업 증명서 or 학생증\n" +
                "\n" +
                "아래 약관 동의 후엔, 선생님 등록이 완료되어 ‘매칭 카카오톡 채널’에서 과외건을 받아보실 수 있습니다. \uD83D\uDE04");
        CommonButton webLinkButton = new WebButton("약관 동의하고 등록완료하기", WEB_LINK, applyAgreeUrl, applyAgreeUrl);
        Message messageBody = new ButtonMessage(message, yeduApplyKey, applyAgree, new CommonButton[]{webLinkButton});
        return createCommonRequest(messageBody, applyAgreeEvent.phoneNumber());
    }

    public CommonRequest mapToApplyPhotoSubmit(PhotoSubmitEvent photoSubmitEvent) {
        String message = ("안녕하세요 선생님 \uD83D\uDE42\n" +
                "\n" +
                "지원서가 정상적으로 제출되었어요.\n" +
                "Y-Edu의 선생님으로 지원해주셔서 감사합니다. \n" +
                "\n" +
                "지원서는 Y-Edu가 프로필로 만들어, 과외 매칭 시 학부모님께 전달드릴게요. \n" +
                "\n" +
                "다음 단계로 프로필 사진과 영상을 구글폼으로 제출해주세요. \uD83D\uDE00\n" +
                "\n" +
                "✅ 제출 포인트\n" +
                "- [필수] 프로필 사진 \n" +
                "- [선택] 영어 수업 소개영상 \n" +
                "\n" +
                "구체적인 설명은 아래 버튼을 눌러 구글폼에서 확인할 수 있으며, 꼭 3일 이내 제출 부탁드려요 \uD83D\uDE47\uD83C\uDFFB\u200D♀\uFE0F");
        CommonButton webLinkButton = new WebButton("사진/영상 제출하기", WEB_LINK, photoSubmitUrl, photoSubmitUrl);
        Message messageBody = new ButtonMessage(message, yeduApplyKey, applyPhotoSubmit, new CommonButton[]{webLinkButton});
        return createCommonRequest(messageBody, photoSubmitEvent.phoneNumber());
    }

    public CommonRequest mapToPhotoHurry(PhotoHurryEvent photoHurryEvent) {
        String message = ("[D-1] 사진 & 영상 제출 리마인드\n" +
                "\n" +
                "잊지말고 내일까지 아래 링크를 통해 소개 사진 및 영상 제출(선택)을 제출해 주세요. \n" +
                "\n" +
                "지금 당장 좋은 사진이 없다면 가장 깔끔한 사진으로 제출하고, 이후 수정해주셔도 괜찮습니다. \n" +
                "\n" +
                "선생님과 좋은 인연으로 만날 수 있길 기대할게요.\uD83D\uDE42");
        CommonButton webLinkButton = new WebButton("사진/영상 제출하기", WEB_LINK, photoHurryUrl, photoHurryUrl);
        Message messageBody = new ButtonMessage(message, yeduApplyKey, applyPhotoHurry, new CommonButton[]{webLinkButton});
        return createCommonRequest(messageBody, photoHurryEvent.phoneNumber());
    }

    public CommonRequest mapToNotifyClass(NotifyClassInfoEvent notifyClassInfoEvent) {
        String message;
        if (notifyClassInfoEvent.online().equals(Online.비대면)) {
            message = ("[" + notifyClassInfoEvent.applicationFormId() + " 과외건 공지]\n" +
                    "\n" +
                    "안녕하세요 " + notifyClassInfoEvent.nickName() + "선생님!\n" +
                    "현재 " + notifyClassInfoEvent.classType() + " " + notifyClassInfoEvent.district().getDescription() + "에 과외건이 들어와 공지드립니다. \uD83D\uDD14\n" +
                    "\n" +
                    "아래 버튼을 통해 과외건 정보를 확인하고, '신청하기' / '넘기기' 중 하나를 3시간 안에 버튼을 눌러 선택해주세요.\n" +
                    "\n" +
                    "* 무응답이 반복되면, 과외공지에 전송이 줄어들 수 있습니다.\n" +
                    "* '넘기기'를 연속으로 한다고 하여 받는 불이익은 없습니다.\n" +
                    "\n" +
                    "\uD83E\uDD1E\uD83C\uDFFB신청 시, 철회는 불가합니다! 반드시 수업 시간과 장소를 확인 후 가능한 수업을 신청해주세요");
        } else {
            message = ("[" + notifyClassInfoEvent.applicationFormId() + " 과외건 공지]\n" +
                    "\n" +
                    "안녕하세요 " + notifyClassInfoEvent.nickName() + "선생님!\n" +
                    "현재 " + notifyClassInfoEvent.classType() + " " + notifyClassInfoEvent.district().getDescription() + " " + notifyClassInfoEvent.dong() + "에 과외건이 들어와 공지드립니다. \uD83D\uDD14\n" +
                    "\n" +
                    "아래 버튼을 통해 과외건 정보를 확인하고, '신청하기' / '넘기기' 중 하나를 3시간 안에 버튼을 눌러 선택해주세요.\n" +
                    "\n" +
                    "* 무응답이 반복되면, 과외공지에 전송이 줄어들 수 있습니다.\n" +
                    "* '넘기기'를 연속으로 한다고 하여 받는 불이익은 없습니다.\n" +
                    "\n" +
                    "\uD83E\uDD1E\uD83C\uDFFB신청 시, 철회는 불가합니다! 반드시 수업 시간과 장소를 확인 후 가능한 수업을 신청해주세요");
        }
        String classUrl = "https://www.yedu-tutor.com/teacher/"+ notifyClassInfoEvent.teacherId()+"/"+ notifyClassInfoEvent.applicationFormId()+"/"+ notifyClassInfoEvent.phoneNumber();
        CommonButton webButton = new WebButton("과외 정보 확인하기", WEB_LINK, classUrl, classUrl);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, notifyClass, new CommonButton[]{webButton});
        return createCommonRequest(messageBody, notifyClassInfoEvent.phoneNumber());
    }

    public CommonRequest mapToMatchingAcceptCase(MatchingAcceptCaseInfoEvent matchingAcceptCaseInfoEvent) {
        String message;
        if (matchingAcceptCaseInfoEvent.online().equals(Online.비대면)) {
             message = ("[과외 신청완료]" +
                    "\n" +
                    "\n" +
                     matchingAcceptCaseInfoEvent.district().getDescription() + " " + matchingAcceptCaseInfoEvent.classType() + " " + matchingAcceptCaseInfoEvent.age() + " 과외건 신청이 완료되었습니다! \uD83D\uDE42\n" +
                    "\n" +
                    "학부모님이 신청해주신 선생님 프로필들을 전달 받으신 후, 답변 주실 예정입니다.\n" +
                    "\n" +
                    "학부모님께 답변이 오면 매칭 성사 여부를 3일 이내에 공유드릴게요.\n" +
                    "\n" +
                    "3일 이내 매칭이 진행되지 않는다면, 여러 사유에 의한 미진행으로 생각해주시면 좋을 것 같습니다! \uD83D\uDE4F");
        } else {
            message = ("[과외 신청완료]" +
                    "\n" +
                    "\n" +
                    matchingAcceptCaseInfoEvent.district().getDescription() + " " + matchingAcceptCaseInfoEvent.dong() + " " + matchingAcceptCaseInfoEvent.classType() + " " + matchingAcceptCaseInfoEvent.age() + " 과외건 신청이 완료되었습니다! \uD83D\uDE42\n" +
                    "\n" +
                    "학부모님이 신청해주신 선생님 프로필들을 전달 받으신 후, 답변 주실 예정입니다.\n" +
                    "\n" +
                    "학부모님께 답변이 오면 매칭 성사 여부를 3일 이내에 공유드릴게요.\n" +
                    "\n" +
                    "3일 이내 매칭이 진행되지 않는다면, 여러 사유에 의한 미진행으로 생각해주시면 좋을 것 같습니다! \uD83D\uDE4F");
        }
        Message messageBody = new TextMessage(message, yeduMatchingKey, matchingAcceptCase);
        return createCommonRequest(messageBody, matchingAcceptCaseInfoEvent.phoneNumber());
    }

    public CommonRequest mapToRefuseCase(MatchingRefuseCaseEvent matchingRefuseCaseEvent) {
        String message = ("[수업 요건 변경 안내]\n" +
                "\n" +
                matchingRefuseCaseEvent.nickName() + " 선생님. \n" +
                "\n" +
                "가능하지 않은 과외 공지가 자꾸 발송된다면 수업 요건 업데이트를 부탁드립니다.\n" +
                "\n" +
                "가능하신 일정과 지역에 변동 사항을 현재 가능한 요건으로 수정하시면 더 필요한 과외건 공지를 받아보실 수 있습니다. \n" +
                "\n" +
                "요건 수정이나, 과외 공지 중단 요청은 상담 직원에게 말씀해주세요.");
        CommonButton simpleButton = new SimpleButton("상담 매니저에게 요청하기", BOT);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, matchingRefuseCase, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, matchingRefuseCaseEvent.phoneNumber());
    }

    public CommonRequest mapToRefuseCaseNow(MatchingRefuseCaseNowEvent matchingRefuseCaseEvent) {
        String message = ("[과외건 공지 수신 설정 안내]\n" +
                "\n" +
                matchingRefuseCaseEvent.nickName() + " 선생님.\n" +
                "\n" +
                "’지금은 수업이 불가’한 이유로 매칭을 거절하셨네요!\n" +
                "\n" +
                "혹시 현재 과외가 어려운 상황으로 잠시 과외건 공지를 받지 않으시려면 아래 버튼을 클릭하여 과외 설정 페이지 접속 후\n" +
                "\n" +
                "‘과외건 공지 받기’ 메뉴에서 비활성화 하시면 메세지 전송이 중단됩니다. \uD83D\uDE42");
        CommonButton webButton = new WebButton("과외 설정 페이지로 이동", WEB_LINK, refuseChangeFormUrl, refuseChangeFormUrl);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, matchingRefuseCaseNow, new CommonButton[]{webButton});
        return createCommonRequest(messageBody, matchingRefuseCaseEvent.phoneNumber());
    }

    public CommonRequest mapToRefuseCaseDistrict(MatchingRefuseCaseDistrictEvent matchingRefuseCaseEvent) {
        String message = ("[과외 가능지역 변경 안내]\n" +
                "\n" +
                matchingRefuseCaseEvent.nickName() + " 선생님.\n" +
                "\n" +
                "현재 발송되고 있는 과외건 공지가 실제 가능한 지역과 다를 경우, 과외 가능 지역 업데이트가 필요합니다.\n" +
                "\n" +
                "아래 버튼을 클릭하여 과외 설정 페이지 접속 후 지역을 변경하실 수 있습니다.\n" +
                "\n" +
                "과외 설정을 최신화 하시면, 가능한 지역의 과외건 공지만 받아보실 수 있습니다. \uD83D\uDE09");
        CommonButton webButton = new WebButton("과외 설정 페이지로 이동", WEB_LINK, refuseChangeFormUrl, refuseChangeFormUrl);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, matchingRefuseCaseDistrict, new CommonButton[]{webButton});
        return createCommonRequest(messageBody, matchingRefuseCaseEvent.phoneNumber());
    }

    public CommonRequest mapToMatchingChannel(InviteMatchingChannelInfoEvent inviteMatchingChannelInfo) {
        String message = ("[매칭 알림톡 초대] \n" +
                "\n" +
                "이름 : " + inviteMatchingChannelInfo.name() + "\n" +
                "영어이름 : " + inviteMatchingChannelInfo.nickName() + "\n" +
                "전화번호 : " + inviteMatchingChannelInfo.phoneNumber() + "\n" +
                "\n" +
                inviteMatchingChannelInfo.nickName() + " 선생님 등록을 위한 모든 절차가 완료되었습니다. \uD83C\uDF89\n" +
                "\n" +
                "해당 채널은 선생님이 신청하셨던 지역의 ‘과외 공지’가 전달되는 채널입니다.  \n" +
                "\n" +
                "이 채널만큼은 꼼꼼히 확인 및 반응해주셔야 해요!\n" +
                "\n" +
                "공지되는 과외건에 대해 3시간 이내에 반복적으로 반응이 없을 경우, 과외건 공지가 줄어들게 된다는 사실을 명심해주세요! ☝\uD83C\uDFFB\n" +
                "\n" +
                "앞으로 잘부탁드립니다 선생님! \uD83D\uDE42");
        CommonButton simpleButton = new SimpleButton("채널 추가", CHANNEL);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, matchingChannel, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, inviteMatchingChannelInfo.phoneNumber());
    }

    public CommonRequest mapToRecommendGuid(RecommendGuideEvent recommendGuideEvent) {
        String message = ("[선생님 프로필을 둘러보셨다면] ☝\uD83C\uDFFB\n" +
                "\n" +
                "원하시는 선생님 메세지에 ‘이 선생님과 수업할래요’ 버튼을 눌러주세요! \n" +
                "\n" +
                "혹시 선택 전, 문의사항이 있으시다면 편하게 물어봐주시면 됩니다. \uD83D\uDE42\n" +
                "\n" +
                "Y-English는 시범과외는 지원하지 않지만 첫 수업 후, 환불이 가능하기에 부담 없이 수업을 진행해 보실 수 있습니다. \uD83D\uDE00\n" +
                "\n" +
                "내일까지 둘러보신 후, 희망 선생님을 알려주세요");
        CommonButton simpleButton = new SimpleButton("매칭 담당자에게 문의하기", BOT);
        Message messageBody = new ButtonMessage(message, yeduOfficialKey, recommendGuid, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, recommendGuideEvent.phoneNumber());
    }

    public CommonRequest mapToRecommendTeacher(RecommendTeacherEvent recommendTeacherEvent) {
        String title = "추천 : " + recommendTeacherEvent.teacherNickName() + " 선생님";
        String message = ("신청해주신 " + recommendTeacherEvent.district() + " 과외 매칭을 위한 선생님을 안내드립니다.\n" +
                "\n" +
                "☀\uFE0F" + recommendTeacherEvent.teacherNickName() + "☀\uFE0F을  아이의 " + recommendTeacherEvent.classType() + "을 책임지고 지도해줄 선생님으로 추천드려요! \n" +
                "\n" +
                "Y-Edu가 상담 내용과 신청서를 꼼꼼히 살펴보고 추천드리는 선생님이에요. \uD83D\uDE00\n" +
                "\n" +
                "아래 버튼을 눌러 '상세프로필'을 천천히 살펴보시고 매칭 희망하시는 선생님을 카카오 채팅으로 말씀해주세요");
        String teacherUrl = "https://www.yedu-tutor.com/teacher/" + recommendTeacherEvent.teacherId() + "?subject=" + recommendTeacherEvent.classType().getDescription();
        CommonButton webButton = new WebButton("선생님 프로필 확인하기", WEB_LINK, teacherUrl, teacherUrl);
        CommonButton simpleButton = new SimpleButton("이 선생님과 수업할래요", MESSAGE);
        Message messageBody = new EmphasizeButtonMessage(message, title, yeduOfficialKey, recommendTeacher, new CommonButton[]{webButton, simpleButton});
        return createCommonRequest(messageBody, recommendTeacherEvent.parentsPhoneNumber());
    }

    public CommonRequest mapToNotifyCalling(NotifyCallingEvent notifyCallingEvent) {
        String message = ("신청서 접수 완료! ✅\n" +
                "\n" +
                "매칭 매니저가 꼼꼼하게 확인하고 있어요. 48시간 이내로 전화드릴게요. \n" +
                "\n" +
                "구체적인 수업 방향성과 교재는 선생님 매칭 후 상담하시게 됩니다. \n" +
                "\n" +
                "매칭 매니저와 전화상담에서는 아이에게 딱 맞는 선생님에 대해 상담과 추천을 받을 수 있어요\uD83D\uDE42");
        Message messageBody = new TextMessage(message, yeduOfficialKey, notifyCalling);
        return createCommonRequest(messageBody, notifyCallingEvent.phoneNumber());
    }

    private CommonRequest createCommonRequest(Message messageBody, String phoneNumber) {
        String refKey = UUID.randomUUID().toString().replace("-", "");
        ContentRequest contentRequest = new ContentRequest(messageBody);
        return new CommonRequest(id, "at", number , phoneNumber, contentRequest, refKey);
    }
}
