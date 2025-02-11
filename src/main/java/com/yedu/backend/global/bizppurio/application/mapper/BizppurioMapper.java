package com.yedu.backend.global.bizppurio.application.mapper;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherInfo;
import com.yedu.backend.global.bizppurio.application.dto.req.CommonRequest;
import com.yedu.backend.global.bizppurio.application.dto.req.ContentRequest;
import com.yedu.backend.global.bizppurio.application.dto.req.content.*;
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

    private static final String WEB_LINK = "WL";
    private static final String BOT = "BK";
    private static final String MESSAGE = "MD";

    public CommonRequest mapToApplyChannel(Teacher teacher) {
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        String message = ("[선생님 지원톡 초대] \n" +
                "\n" +
                "이름 : " + teacherInfo.getName() + "\n" +
                "영어이름 : " + teacherInfo.getNickName() + "\n" +
                "전화번호 : " + teacherInfo.getPhoneNumber() + " \n" +
                "\n" +
                teacherInfo.getNickName() + " 선생님 등록을 위한 모든 절차가 완료되었습니다. \n" +
                "\n" +
                "해당 채널은 ‘상담 직원에게 문의 및 매칭 후 수업 진행 사항을 기록’하는 채널이에요.\n" +
                "\n" +
                "이후 편하게 사용하실 수 있도록 미리 초대드립니다. \uD83D\uDE42\n" +
                "\n" +
                "아래 버튼을 눌러, 선생님 등록을 최종적으로 완료해주세요! 회신 카톡 메세지가 없어도 등록이 완료된 것이니 안심해주세요.\n" +
                "\n" +
                "매칭 이후 별도로 다시 안내드릴게요. 감사합니다!");
        CommonButton simpleButton = new SimpleButton("선생님 등록 최종 완료하기", BOT);
        Message messageBody = new ButtonMessage(message, yeduTutorKey, applyChannel, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, teacherInfo.getPhoneNumber());
    }

    public CommonRequest mapToApplyAgree(Teacher teacher) {
        String message = ("이제 한 단계만 남았어요! \n" +
                "\n" +
                "선생님께 과외를 찾아드리고, 보수를 전산드리기 위해 위임 약관 동의가 필요합니다. \uD83D\uDE00\n" +
                "\n" +
                "아래 서류를 미리 준비해주세요\n" +
                "1. 신분증 사본 (원천세 신고용)\n" +
                "2 재학/졸업 증명서 (증빙용)\n" +
                "\n" +
                "재학, 졸업 증명서 발행이 불가한 경우엔 상담 요청을해주세요. \n" +
                "\n" +
                "아래 약관 동의 후엔, 선생님 등록이 완료되어 ‘매칭 채널’에서 과외건을 받아보실 수 있습니다. \uD83D\uDE04");
        CommonButton webLinkButton = new WebButton("약관 동의하고 등록완료하기", WEB_LINK, applyAgreeUrl, applyAgreeUrl);
        Message messageBody = new ButtonMessage(message, yeduApplyKey, applyAgree, new CommonButton[]{webLinkButton});
        return createCommonRequest(messageBody, teacher.getTeacherInfo().getPhoneNumber());
    }

    public CommonRequest mapToApplyPhotoSubmit(Teacher teacher) {
        String message = ("지원서는 Y-Edu가 프로필로 만들어, 이후 매칭 시 학부모님께 전달드릴게요. \n" +
                "\n" +
                "다음 단계는 프로필 사진과 영상을 구글폼으로 제출해 주세요. \uD83D\uDE42\n" +
                "\n" +
                "\uD83D\uDCF7 프로필 사진 포인트\n" +
                "- 증명사진도 좋아요 \n" +
                "- 사진의 배경이 원색으로 깔끔해야합니다. \n" +
                "\n" +
                "\uD83D\uDCF9 영상 제출 포인트 (선택)\n" +
                "- 30초 정도의 영상입니다. \n" +
                "- 영어 과목은 80% 이상 영어로!\n" +
                "- 비율은 가로가 길게 촬영\n" +
                "- 제출 시 매칭 확률 47% up\n" +
                "\n" +
                "영상은 첫 수업 아이에게 앞으로의 수업을 설명해주는 내용이면 됩니다. (구글폼에 가이드 참고)\n" +
                "\n" +
                "사진과 영상은 꼭 3일 이내 제출해주세요!");
        CommonButton webLinkButton = new WebButton("사진/영상 제출하기", WEB_LINK, photoSubmitUrl, photoSubmitUrl);
        Message messageBody = new ButtonMessage(message, yeduApplyKey, applyPhotoSubmit, new CommonButton[]{webLinkButton});
        return createCommonRequest(messageBody, teacher.getTeacherInfo().getPhoneNumber());
    }

    public CommonRequest mapToPhotoHurry(Teacher teacher) {
        String message = ("[D-1] 사진 & 영상 제출 리마인드\n" +
                "\n" +
                "잊지말고 내일까지 아래 링크를 통해 소개 사진 및 영상 제출(선택)을 제출해 주세요. \n" +
                "\n" +
                "지금 당장 좋은 사진이 없다면 가장 깔끔한 사진으로 제출하고, 이후 수정해주셔도 괜찮습니다. \n" +
                "\n" +
                "선생님과 좋은 인연으로 만날 수 있길 기대할게요.\uD83D\uDE42");
        CommonButton webLinkButton = new WebButton("사진/영상 제출하기", WEB_LINK, photoHurryUrl, photoHurryUrl);
        Message messageBody = new ButtonMessage(message, yeduApplyKey, applyPhotoHurry, new CommonButton[]{webLinkButton});
        return createCommonRequest(messageBody, teacher.getTeacherInfo().getPhoneNumber());
    }

    public CommonRequest mapToCounselStart(Teacher teacher) {
        String message = ("안녕하세요 선생님\uD83D\uDE42\n" +
                "\n" +
                "지원서가 정상적으로 제출되었어요.  \n" +
                "Y-Edu의 튜터로 지원해주셔서 감사합니다. \n" +
                "\n" +
                "아래 버튼을 눌러 지원을 이어가주세요");
        CommonButton simpleButton = new SimpleButton("다음 절차는 무엇인가요?", BOT);
        Message messageBody = new ButtonMessage(message, yeduApplyKey, counselStart, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, teacher.getTeacherInfo().getPhoneNumber());
    }

    public CommonRequest mapToNotifyClass(ApplicationForm applicationForm, Teacher teacher) {
        String message = ("["+applicationForm.getDistrict().getDescription()+" "+applicationForm.getDong()+" 과외건 공지]" +
                "\n" +
                "\n" +
                "안녕하세요 #{선생님 영어이름}선생님!\n" +
                "현재 "+applicationForm.getWantedSubject()+" "+applicationForm.getDistrict().getDescription()+" "+applicationForm.getDong()+"에 과외건이 들어와 공지드립니다. \uD83D\uDD14\n" +
                "\n" +
                "아래 버튼을 통해 과외건 정보를 확인하고, '신청하기' / '넘기기' 중 하나를 3시간 안에 응답해주세요.\n" +
                "\n" +
                "* 무응답이 반복되면, 과외공지에 전송이 줄어들 수 있습니다.\n" +
                "* '넘기기'를 연속으로 한다고 하여 받는 불이익은 없습니다.\n" +
                "\n" +
                "\uD83E\uDD1E\uD83C\uDFFB신청 시, 철회는 불가합니다! 반드시 수업 시간과 장소를 확인 후 가능한 수업을 신청해주세요");
        String classUrl = "https://www.yedu-tutor.com/teacher/"+teacher.getTeacherId()+"/"+applicationForm.getApplicationFormId()+"/"+teacher.getTeacherInfo().getPhoneNumber();
        CommonButton webButton = new WebButton("과외 정보 확인하기", WEB_LINK, classUrl, classUrl);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, notifyClass, new CommonButton[]{webButton});
        return createCommonRequest(messageBody, teacher.getTeacherInfo().getPhoneNumber());
    }

    public CommonRequest mapToMatchingAcceptCase(ApplicationForm applicationForm, Teacher teacher) {
        String message = ("[과외 신청완료]" +
                "\n" +
                "\n" +
                applicationForm.getDistrict().getDescription() + " " + applicationForm.getDong() + " " + applicationForm.getWantedSubject().name() + " " + applicationForm.getAge() + " 과외건 신청이 완료되었습니다! \uD83D\uDE42\n" +
                "\n" +
                "학부모님이 신청해주신 선생님 프로필들을 전달 받으신 후, 답변 주실 예정입니다.\n" +
                "\n" +
                "학부모님께 답변이 오면 매칭 성사 여부를 3일 이내에 공유드릴게요.\n" +
                "\n" +
                "3일 이내 매칭이 진행되지 않는다면, 여러 사유에 의한 미진행으로 생각해주시면 좋을 것 같습니다! \uD83D\uDE4F");
        Message messageBody = new TextMessage(message, yeduMatchingKey, matchingAcceptCase);
        return createCommonRequest(messageBody, teacher.getTeacherInfo().getPhoneNumber());
    }

    public CommonRequest mapToRefuseCase(Teacher teacher) {
        String message = ("[수업 요건 변경 안내]\n" +
                "\n" +
                teacher.getTeacherInfo().getNickName() + " 선생님. \n" +
                "\n" +
                "가능하지 않은 과외 공지가 자꾸 발송된다면 수업 요건 업데이트를 부탁드립니다.\n" +
                "\n" +
                "가능하신 일정과 지역에 변동 사항을 현재 가능한 요건으로 수정하시면 더 필요한 과외건 공지를 받아보실 수 있습니다. \n" +
                "\n" +
                "요건 수정이나, 과외 공지 중단 요청은 상담 직원에게 말씀해주세요.");
        CommonButton simpleButton = new SimpleButton("상담 매니저에게 요청하기", BOT);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, matchingRefuseCase, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, teacher.getTeacherInfo().getPhoneNumber());
    }

    public CommonRequest mapToMatchingChannel(Teacher teacher) {
        TeacherInfo teacherInfo = teacher.getTeacherInfo();
        String message = ("[매칭 알림톡 초대] \n" +
                "\n" +
                "이름 : " + teacherInfo.getName() + "\n" +
                "영어이름 : " + teacherInfo.getNickName() + "\n" +
                "전화번호 : " + teacherInfo.getPhoneNumber() + "\n" +
                "\n" +
                teacherInfo.getNickName() + " 선생님 등록을 위한 모든 절차가 완료되었습니다. \n" +
                "\n" +
                "해당 채널은 선생님이 신청하셨던 지역의 ‘과외 건이 공지’되는 채널입니다.  \n" +
                "\n" +
                "이 채널만큼은 꼼꼼히 확인 및 반응해주셔야 해요!\n" +
                "\n" +
                "공지되는 과외건에 대해 3시간 이내에 반복적으로 반응이 없을 경우, 과외건 공지가 줄어들게 된다는 사실을 명심해주세요! ☝\uD83C\uDFFB\n" +
                "\n" +
                "아래 버튼을 눌러, 과외 공지를 받기 시작해주세요. 회신 메세지가 없어도 설정이 완료된 것이니 안심해주세요.  \n" +
                "\n" +
                "앞으로 잘부탁드립니다 선생님! \uD83D\uDE42");
        CommonButton simpleButton = new SimpleButton("과외 받기 시작하기", BOT);
        Message messageBody = new ButtonMessage(message, yeduMatchingKey, matchingChannel, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, teacherInfo.getPhoneNumber());
    }

    public CommonRequest mapToRecommendGuid(Parents parents) {
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
        return createCommonRequest(messageBody, parents.getPhoneNumber());
    }

    public CommonRequest mapToBeforeCheck(Parents parents) {
        String message = ("곧 전화드릴게요 \uD83D\uDE42\n" +
                "\n" +
                "오늘, 매칭 매니저가 " + parents.getPhoneNumber() + "로 전화드릴 예정이에요. \n" +
                "\n" +
                "\uD83D\uDCDE매칭매니저 : " + phoneNumber + "\n" +
                "\n" +
                "아이에게 맞는 선생님을 찾을 땐, 아이의 성향과 의사도 중요해요. \n" +
                "\n" +
                "전화 상담 전 아이가 어떤 선생님을 원하는지 물어봐주시면 큰 도움이 됩니다 ☀\uFE0F\n" +
                "\n" +
                "24시간 내로 전화드릴 예정이에요. 확인하셨다면 꼭 아래 '확인했어요' 버튼을 눌러주세요!");

        CommonButton simpleButton = new SimpleButton("확인했어요", BOT);
        Message messageBody = new ButtonMessage(message, yeduOfficialKey, beforeCheck, new CommonButton[]{simpleButton});
        return createCommonRequest(messageBody, parents.getPhoneNumber());
    }

    public CommonRequest mapToWriteApplicationForm(Parents parents) {
        String message = ("맞춤 상담 진행을 위해, 신청서를 작성해주세요! \uD83D\uDE00\n" +
                "\n" +
                "상담 신청이 접수됐어요. \n" +
                "신청서를 작성해주시면 심층 맞춤상담이 가능합니다. \n" +
                "\n" +
                "[맞춤 상담 신청서 작성시]\n" +
                "✅ 상담 시 미리 추천 선생님 제공\n" +
                "✅ 더욱 정확한 심층 매칭\n" +
                "✅ 1순위 상담 진행");
        CommonButton webLinkButton = new WebButton("1분만에 작성하기", WEB_LINK, writeApplicationFormUrl, writeApplicationFormUrl);
        Message messageBody = new ButtonMessage(message, yeduOfficialKey, writeApplicationForm, new CommonButton[]{webLinkButton});
        return createCommonRequest(messageBody, parents.getPhoneNumber());
    }

    public CommonRequest mapToRecommendTeacher(ApplicationForm applicationForm, Teacher teacher) {
        Parents parents = applicationForm.getParents();
        String message = (teacher.getTeacherInfo().getNickName() + "을 아이의 " + applicationForm.getWantedSubject().name() + "을 책임지고 지도해줄 선생님으로 추천드려요! \uD83E\uDDD0\n" +
                "\n" +
                "Y-English에서 꼼꼼히 살펴보고 추천드리는 선생님입니다! \n" +
                "\n" +
                "아래 상세프로필에서 선생님에 대해 자세하게 확인할 수 있어요. \n" +
                "\n" +
                "천천히 살펴보시고 매칭 희망하시는 선생님을 카카오 채팅으로 말씀해주세요 \uD83D\uDE42");
        String teacherUrl = "https://www.yedu-tutor.com/teacher/" + teacher.getTeacherId() + "?subject=" + applicationForm.getWantedSubject().getDescription();
        CommonButton simpleButton = new SimpleButton("이 선생님과 수업할래요", MESSAGE);
        CommonButton webButton = new WebButton("선생님 프로필 확인하기", WEB_LINK, teacherUrl, teacherUrl);
        Message messageBody = new ButtonMessage(message, yeduOfficialKey, recommendTeacher, new CommonButton[]{webButton, simpleButton});
        return createCommonRequest(messageBody, parents.getPhoneNumber());
    }

    public CommonRequest mapToNotifyCalling(Parents parents) {
        String message = ("신청서 접수 완료! ✅\n" +
                "\n" +
                "매칭 매니저가 꼼꼼하게 확인하고 있어요.  48시간 이내로 전화드릴게요. \n" +
                "\n" +
                "구체적인 수업 방향성과 교재는 선생님 매칭 후 상담하시게 됩니다. \n" +
                "\n" +
                "매칭 매니저와 전화상담에서는 아이에게 딱 맞는 선생님에 대해 상담과 추천을 받을 수 있어요\uD83D\uDE42");
        Message messageBody = new TextMessage(message, yeduOfficialKey, notifyCalling);
        return createCommonRequest(messageBody, parents.getPhoneNumber());
    }

    private CommonRequest createCommonRequest(Message messageBody, String phoneNumber) {
        String refKey = UUID.randomUUID().toString().replace("-", "");
        ContentRequest contentRequest = new ContentRequest(messageBody);
        return new CommonRequest(id, "at", number , phoneNumber, contentRequest, refKey);
    }
}
