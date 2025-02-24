package com.yedu.backend.global.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordWebhookClient {
    private final WebClient webClient;

    @Value("${discord.webhook.url}")
    private String webhookUrl;
    @Value("${discord.webhook.teacher}")
    private String webHookTeacherUrl;

    public void sendServerAlarm(DiscordWebhookRequest webhookRequest) {
        sendWebhook(webhookUrl, webhookRequest);
    }

    public void sendTeacherRegisterAlarm(DiscordWebhookRequest webhookRequest) {
        sendWebhook(webHookTeacherUrl, webhookRequest);
    }

    private void sendWebhook(String url, DiscordWebhookRequest request) {
        webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> log.info("Discord 웹훅 전송 성공"))
                .doOnError(error -> log.error("Discord 웹훅 전송 실패: {}", error.getMessage()))
                .subscribe();
    }
}
