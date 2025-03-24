package com.yedu.backend.global.bizppurio.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.backend.global.bizppurio.application.constant.BizppurioResponseCode;
import com.yedu.backend.global.bizppurio.application.dto.req.CommonRequest;
import com.yedu.backend.global.bizppurio.application.dto.req.MessageStatusRequest;
import com.yedu.backend.global.bizppurio.application.dto.res.MessageResponse;
import com.yedu.backend.global.config.redis.RedisRepository;
import com.yedu.backend.global.discord.DiscordWebhookUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Supplier;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Slf4j
@Component
public class BizppurioSend {

    private final BizppurioAuth bizppurioAuth;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final DiscordWebhookUseCase discordWebhookUseCase;
    private final RedisRepository redisRepository;
    private final static String SUCCESS = "7000";
    @Value("${bizppurio.message}")
    private String messageUrl;
    
    protected Mono<Void> sendMessageWithExceptionHandling(Supplier<CommonRequest> messageSupplier) {
        CommonRequest commonRequest = messageSupplier.get();
        String accessToken = bizppurioAuth.getAuth();
        String request;
        try {
            request = objectMapper.writeValueAsString(commonRequest);
        } catch (Exception e) {
            log.error("Json 직렬화 실패");
            return Mono.empty();
        }
        log.info("알림톡 발송 : {} \n{}", commonRequest.to(), commonRequest.content().message());
        String refkey = commonRequest.refkey();
        String message = commonRequest.content().message().getMessage();
        redisRepository.setValues(refkey, message, Duration.ofSeconds(30l));
        return webClient.post()
                .uri(messageUrl)
                .headers(h -> h.setContentType(APPLICATION_JSON))
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(request)
                .retrieve()
                // 200이 아닐 경우 예외 발생
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                            .map(errorBody -> new IllegalArgumentException("비즈뿌리오 알림톡 전송 API 요청 실패: " + errorBody))
                )
                .bodyToMono(MessageResponse.class)
                .doOnSubscribe(subscription -> log.info("알림톡 요청 시작"))
                .doOnSuccess(response -> log.info("알림톡 초기 요청 성공"))
                .doOnError(error -> {
                    log.error("알림톡 초기 요청 실패 : {}", error.getMessage());
                    discordWebhookUseCase.sendAlarmTalkErrorWithFirst(commonRequest.to(), commonRequest.content().message().getMessage(), error.getMessage());
                })
                .then();
    }

    public void checkByWebHook(MessageStatusRequest request) {
        if (request.RESULT().equals(SUCCESS)) {
            log.info("{} 에 대한 알림톡 전송 완료", request.PHONE());
            return;
        }
        BizppurioResponseCode bizppurioResponseCode = BizppurioResponseCode.findByCode(Integer.parseInt(request.RESULT()))
                .orElseGet(() -> BizppurioResponseCode.NOTFOUND_ERROR);
        String errorMessage = bizppurioResponseCode.getMessage();
        int code = bizppurioResponseCode.getCode();
        String message = redisRepository.getValues(request.REFKEY()).orElse("내용 알 수 없음");
        log.error("{} 에 대한 알림톡 전송 실패, 내용 {} \nRefKey : {} ResultCode : {} {}",  request.PHONE(), message, request.REFKEY(), code, errorMessage);
        discordWebhookUseCase.sendAlarmTalkError(request.PHONE(), message, String.valueOf(code), errorMessage);
    }
}
