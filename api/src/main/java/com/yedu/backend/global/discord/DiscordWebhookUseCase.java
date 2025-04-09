package com.yedu.backend.global.discord;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherDistrict;
import com.yedu.discord.support.DiscordWebClientTemplate;
import com.yedu.discord.support.DiscordWebhookType;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest.Field;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.yedu.discord.support.DiscordMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordWebhookUseCase {
    private final DiscordWebClientTemplate webhookClient;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

    @Value("${discord.profile}")
    private String profileUrl;

    @Async
    public void sendAlarmTalkTokenError(String errorMessage) {
        List<Field> fields = List.of(
                mapToField("에러 메시지 및 코드", errorMessage),
                mapToField("비즈뿌리오 코드 참고", "https://biztech.gitbook.io/webapi/status-code/api\nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft")
        );
        DiscordWebhookRequest request = mapToDiscordWithServerAlarm("비즈뿌리오 토큰 발급 실패", "비즈뿌리오 토큰 발급에 실패하였습니다", fields);
        webhookClient.sendWebhook(DiscordWebhookType.SERVER_ALARM ,request);
    }

    @Async
    public void sendAlarmTalkError(String phoneNumber, String content, String code, String message) {
        List<Field> fields = List.of(
                mapToField("핸드폰번호", phoneNumber),
                mapToField("알림톡 내용", content),
                mapToField("에러 코드", code),
                mapToField("에러 메시지", message),
                mapToField("비즈뿌리오 코드 참고", "https://biztech.gitbook.io/webapi/status-code/api\nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft")
        );
        DiscordWebhookRequest request = mapToDiscordWithServerAlarm("알림톡 발송 실패", "알림톡 발송에 실패하였습니다.", fields);
        webhookClient.sendWebhook(DiscordWebhookType.SERVER_ALARM, request);
    }

    @Async
    public void sendAlarmTalkErrorWithFirst(String phoneNumber, String content, String code) {
        List<Field> fields = List.of(
                mapToField("핸드폰번호", phoneNumber),
                mapToField("알림톡 내용", content),
                mapToField("에러 코드 및 코드", code),
                mapToField("비즈뿌리오 코드 참고", "https://biztech.gitbook.io/webapi/status-code/api\nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft")
        );
        DiscordWebhookRequest request = mapToDiscordWithServerAlarm("알림톡 발송 실패", "알림톡 발송에 실패하였습니다.", fields);
        webhookClient.sendWebhook(DiscordWebhookType.SERVER_ALARM, request);
    }

    @Async
    public void sendScheduleCancel(Teacher teacher, ApplicationForm applicationForm, String refuseReason) {
        List<Field> fields = List.of(
            mapToField("✅ 선생님 이름", teacher.getTeacherInfo().getName()),
            mapToField("✅ 학부모 이름", applicationForm.getParents().getKakaoName()),
            mapToField("✅ 매칭 취소 사유", refuseReason),
            mapToField("✅ 매칭 취소 일시", currentTime())
        );
        DiscordWebhookRequest request = mapToDiscordWithInformation("⚠️ 매칭이 취소되었어요 ⚠️", fields);
        webhookClient.sendWebhook(DiscordWebhookType.SCHEDULE_CANCEL,request);
    }

    @Async
    public void sendTeacherRegister(Teacher teacher, List<TeacherDistrict> districts) {
        StringBuilder subject = new StringBuilder();
        StringBuilder teacherLink = new StringBuilder();
        if (teacher.getTeacherClassInfo().isMathPossible()) {
            subject.append("수학 ");
            teacherLink.append("✅ 수학 : ")
                    .append(profileUrl)
                    .append(teacher.getTeacherId()).append("?subject=math\n");
        }
        if (teacher.getTeacherClassInfo().isEnglishPossible()) {
            subject.append("영어");
            teacherLink.append("✅ 영어 : ")
                    .append(profileUrl)
                    .append(teacher.getTeacherId()).append("?subject=english\n");
        }
        StringBuilder regions = new StringBuilder();
        districts.forEach(district -> regions.append(district.getDistrict()).append("\n"));

        List<Field> fields = List.of(
                mapToField("⏰ " + currentTime(), ""),
                mapToField("선생님 정보", "✅ 이름 : " + teacher.getTeacherInfo().getName() + "\n" +
                        "✅ 영어 이름 : " + teacher.getTeacherInfo().getNickName() + "\n" +
                        "\n✅ 수업 상세\n" + subject + "\n\n✅ 수업 가능 지역\n" + regions + "\n"),
                mapToField("쌤 프로필", teacherLink.toString())
        );

        DiscordWebhookRequest request = mapToDiscordWithInformation("🔥 약관폼이 제출되었어요 🔥", fields);
        webhookClient.sendWebhook(DiscordWebhookType.TEACHER_REGISTER, request);
    }

    private String currentTime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }
}
