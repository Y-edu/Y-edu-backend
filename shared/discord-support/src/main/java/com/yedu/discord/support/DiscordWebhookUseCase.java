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

  public void sendScheduleCancel(ScheduleCancelEvent event) {
    List<Field> fields =
        List.of(
            mapToField("✅ 선생님 이름", event.teacherName()),
            mapToField("✅ 학부모 이름", event.parentsName()),
            mapToField("✅ 매칭 취소 사유", event.refuseReason()),
            mapToField("✅ 매칭 취소 일시", format(LocalDateTime.now())));
    DiscordWebhookRequest request = mapToDiscordWithInformation("⚠️ 매칭이 취소되었어요 ⚠️", fields);
    webhookClient.sendWebhook(DiscordWebhookType.SCHEDULE_CANCEL, request);
  }

  public void sendTeacherRegister(TeacherRegisterEvent event) {
    List<Field> fields =
        List.of(
            mapToField("⏰ " + format(LocalDateTime.now()), ""),
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

  public void sendNotificationDeliverySuccess(NotificationDeliverySuccessEvent event) {
    List<Field> fields =
        List.of(
            mapToField("✅ 발송 채널", event.senderProfileDesc()),
            mapToField("✅ 발송된 템플릿", event.templateDescription()),
            mapToField("✅ 알림톡 내용", event.content()),
            mapToField("✅ 수신 대상", event.receiverTypeDesc()),
            mapToField("✅ 수신자 핸드폰 번호", event.receiverPhoneNumber()),
            mapToField("✅ 수신 일시", format(event.deliveredAt()))
        );
    DiscordWebhookRequest request =
        mapToDiscordWithInformation("알림톡 발송 성공", fields);
    webhookClient.sendWebhook(DiscordWebhookType.NOTIFICATION_ALARM, request);

  }

  private String format(LocalDateTime time) {
    return time.format(dateTimeFormatter);
  }


}
