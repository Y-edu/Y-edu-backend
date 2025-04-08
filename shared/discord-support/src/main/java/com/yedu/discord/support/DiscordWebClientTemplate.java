package com.yedu.discord.support;

import com.yedu.discord.support.dto.req.DiscordWebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordWebClientTemplate {

  private final WebClient discordWebClient;

  private final DiscordWebClientProperties properties;

  public void sendServerAlarm(DiscordWebhookRequest webhookRequest) {
    sendWebhook(properties.webhook().url(), webhookRequest);
  }

  public void sendTeacherRegisterAlarm(DiscordWebhookRequest webhookRequest) {
    sendWebhook(properties.webhook().teacher(), webhookRequest);
  }

  public void sendScheduleCancel(DiscordWebhookRequest webhookRequest) {
    sendWebhook(properties.webhook().scheduleCancel(), webhookRequest);
  }

  private void sendWebhook(String url, DiscordWebhookRequest request){
    discordWebClient.post()
        .uri(url)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(Void.class)
        .doOnSuccess(response -> log.info("Discord 웹훅 전송 성공"))
        .doOnError(error -> log.error("Discord 웹훅 전송 실패: {}", error.getMessage()))
        .subscribe();
  }

}
