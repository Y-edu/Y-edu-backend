package com.yedu.backend.global.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.yedu.backend.global.discord.DiscordMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordWebhookSend {
    private final WebClient webClient;

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    public void sendAlarmTalkTokenError(String errorMessage) {
        List<Field> fields = List.of(mapToField("에러 메시지 및 코드", errorMessage), mapToField("비즈뿌리오 코드 참고", "https://biztech.gitbook.io/webapi/status-code/api \nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft"));
        DiscordWebhookRequest webhookRequest = mapToDiscordWebhook(
                "비즈뿌리오 토큰 발급 실패",
                "비즈뿌리오 토큰 발급에 실패하였습니다",
                fields
        );

        send(webhookRequest);
    }

    public void sendAlarmTalkError(String phoneNumber, String content, String errorCode) {
        List<Field> fields = List.of(mapToField("핸드폰번호", phoneNumber), mapToField("알림톡 내용", content), mapToField("에러 메시지 및 코드", errorCode),         mapToField("비즈뿌리오 코드 참고", "https://biztech.gitbook.io/webapi/status-code/api \nhttps://biztech.gitbook.io/webapi/status-code/at-ai-ft"));
        DiscordWebhookRequest webhookRequest = mapToDiscordWebhook(
                "알림톡 발송 실패",
                "알림톡 발송에 실패하였습니다.",
                fields
        );

        send(webhookRequest);
    }

    private void send(DiscordWebhookRequest webhookRequest) {
        webClient.post()
                .uri(webhookUrl)
                .bodyValue(webhookRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> log.info("Discord 웹훅 전송"))
                .doOnError(error -> log.error("Discord 웹훅 전송 실패 : {}", error.getMessage()))
                .subscribe();
    }
}
