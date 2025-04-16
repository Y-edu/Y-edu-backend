package com.yedu.bizppurio.support.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.dto.res.MessageResponse;
import com.yedu.bizppurio.support.event.publisher.BizppurioModuleEventPublisher;
import com.yedu.common.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.function.Supplier;

import static com.yedu.bizppurio.support.event.mapper.DiscordEventMapper.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Slf4j
@Component
public class BizppurioSend {

    private final BizppurioAuth bizppurioAuth;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final RedisRepository redisRepository;
    private final BizppurioModuleEventPublisher bizppurioModuleEventPublisher;
    @Value("${bizppurio.message}")
    private String messageUrl;
    
    protected String sendMessageWithExceptionHandling(Supplier<CommonRequest> messageSupplier) {
        CommonRequest commonRequest = messageSupplier.get();
        String accessToken = bizppurioAuth.getAuth();
        String request;
        try {
            request = objectMapper.writeValueAsString(commonRequest);
        } catch (Exception e) {
            log.error("Json 직렬화 실패");
            throw new IllegalArgumentException("Json 직렬화 실패");
        }
        log.info("알림톡 발송 : {} \n{}", commonRequest.to(), commonRequest.content().at());
        String refkey = commonRequest.refkey();
        String message = commonRequest.content().at().getMessage();
        redisRepository.setValues(refkey, message, Duration.ofSeconds(30l));
        webClient.post()
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
                    bizppurioModuleEventPublisher.publishAlarmTalkErrorWithFirstEvent(mapToAlarmTalkErrorWithFirstEvent(commonRequest.to(), commonRequest.content().at().getMessage(), error.getMessage()));
                })
                .subscribe();
        return refkey;
    }
}
