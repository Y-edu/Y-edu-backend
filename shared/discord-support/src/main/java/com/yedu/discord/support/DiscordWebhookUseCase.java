package com.yedu.discord.support;

import static com.yedu.discord.support.DiscordMapper.*;

import com.yedu.common.event.discord.*;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordWebhookUseCase {
  private final DiscordWebClientTemplate webhookClient;

  private final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

  public void sendAlarmTalkTokenError(AlarmTalkErrorMessageEvent event) {
    List<Field> fields =
        List.of(
            mapToField("에러 메시지 및 코드", event.errorMessage()),
            mapToField(
                "비즈뿌리오 코드 참고",
                "https://biztech.gitbook.io/webapi/status-code/api\nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft"));
    DiscordWebhookRequest request =
        mapToDiscordWithServerAlarm("비즈뿌리오 토큰 발급 실패", "비즈뿌리오 토큰 발급에 실패하였습니다", fields);
    webhookClient.sendWebhook(DiscordWebhookType.SERVER_ALARM, request);
  }

  public void sendAlarmTalkError(AlarmTalkErrorInfoEvent event) {
    List<Field> fields =
        List.of(
            mapToField("핸드폰번호", event.phoneNumber()),
            mapToField("알림톡 내용", event.content()),
            mapToField("에러 코드", event.code()),
            mapToField("에러 메시지", event.message()),
            mapToField(
                "비즈뿌리오 코드 참고",
                "https://biztech.gitbook.io/webapi/status-code/api\nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft"));
    DiscordWebhookRequest request =
        mapToDiscordWithServerAlarm("알림톡 발송 실패", "알림톡 발송에 실패하였습니다.", fields);
    webhookClient.sendWebhook(DiscordWebhookType.SERVER_ALARM, request);
  }

  public void sendAlarmTalkErrorWithFirst(AlarmTalkErrorWithFirstEvent event) {
    List<Field> fields =
        List.of(
            mapToField("핸드폰번호", event.phoneNumber()),
            mapToField("알림톡 내용", event.content()),
            mapToField("에러 코드 및 코드", event.code()),
            mapToField(
                "비즈뿌리오 코드 참고",
                "https://biztech.gitbook.io/webapi/status-code/api\nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft"));
    DiscordWebhookRequest request =
        mapToDiscordWithServerAlarm("알림톡 발송 실패", "알림톡 발송에 실패하였습니다.", fields);
    webhookClient.sendWebhook(DiscordWebhookType.SERVER_ALARM, request);
  }

  public void sendScheduleCancel(ScheduleCancelEvent event) {
    List<Field> fields =
        List.of(
            mapToField("✅ 선생님 이름", event.teacherName()),
            mapToField("✅ 학부모 이름", event.parentsName()),
            mapToField("✅ 매칭 취소 사유", event.refuseReason()),
            mapToField("✅ 매칭 취소 일시", currentTime()));
    DiscordWebhookRequest request = mapToDiscordWithInformation("⚠️ 매칭이 취소되었어요 ⚠️", fields);
    webhookClient.sendWebhook(DiscordWebhookType.SCHEDULE_CANCEL, request);
  }

  public void sendTeacherRegister(TeacherRegisterEvent event) {
    List<Field> fields =
        List.of(
            mapToField("⏰ " + currentTime(), ""),
            mapToField(
                "선생님 정보",
                "✅ 이름 : "
                    + event.teacherName()
                    + "\n"
                    + "✅ 영어 이름 : "
                    + event.teacherNickName()
                    + "\n"
                    + "\n✅ 수업 상세\n"
                    + event.subject()
                    + "\n\n✅ 수업 가능 지역\n"
                    + event.region()
                    + "\n"),
            mapToField("쌤 프로필", event.teacherLink()));

    DiscordWebhookRequest request = mapToDiscordWithInformation("🔥 약관폼이 제출되었어요 🔥", fields);
    webhookClient.sendWebhook(DiscordWebhookType.TEACHER_REGISTER, request);
  }

  private String currentTime() {
    return LocalDateTime.now().format(dateTimeFormatter);
  }
}
