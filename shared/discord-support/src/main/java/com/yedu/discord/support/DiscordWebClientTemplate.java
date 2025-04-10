package com.yedu.discord.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  private final ObjectMapper objectMapper;

  public void sendWebhook(DiscordWebhookType webhookType, DiscordWebhookRequest request) {
    String url = properties.resolveUrl(webhookType);

    discordWebClient.post()
        .uri(url)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(Void.class)
        .doOnSuccess(response -> log.info("Discord 웹훅 전송 성공"))
        .doOnError(error -> {
          try {
            String jsonRequest = objectMapper.writeValueAsString(request);
            log.error("Discord 웹훅 전송 실패: {} / 요청 경로 : {} /원본 요청: {}", error.getMessage(), url, jsonRequest);
          } catch (JsonProcessingException e) {
            log.error("요청 직렬화 실패: {}", e.getMessage(), e);
          }
        })
        .subscribe();
  }


}
