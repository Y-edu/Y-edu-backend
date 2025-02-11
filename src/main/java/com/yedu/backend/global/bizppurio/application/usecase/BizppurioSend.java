package com.yedu.backend.global.bizppurio.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.backend.global.bizppurio.application.dto.req.CommonRequest;
import com.yedu.backend.global.bizppurio.application.dto.req.MessageStatusRequest;
import com.yedu.backend.global.bizppurio.application.dto.res.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Slf4j
@Component
public class BizppurioSend {
    private final BizppurioAuth bizppurioAuth;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @Value("${bizppurio.message}")
    private String messageUrl;

    protected Mono<Void> sendMessageWithExceptionHandling(Supplier<CommonRequest> messageSupplier) {
        try {
            CommonRequest commonRequest = messageSupplier.get();
            String accessToken = bizppurioAuth.getAuth();
            String request = objectMapper.writeValueAsString(commonRequest);
            log.info("알림톡 발송 : {} \n{}", commonRequest.to(), commonRequest.content().at());
            return webClient.post()
                    .uri(messageUrl)
                    .headers(h -> h.setContentType(APPLICATION_JSON))
                    .headers(h -> h.setBearerAuth(accessToken))
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        log.error("알림톡 발송 실패 : {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .doOnNext(errorBody -> log.error("응답 본문 : {}", errorBody))
                                .flatMap(error -> Mono.error(new IllegalArgumentException("비즈뿌리오 알림톡 전송 API 요청 실패")));
                    })
                    .bodyToMono(MessageResponse.class)
                    .doOnSubscribe(subscription -> log.info("알림톡 요청 시작"))
                    .doOnNext(response -> log.info("알림톡 요청 결과 : {} {} {}", response.code(), response.description(), response.messagekey()))
                    .doOnError(error -> log.error("알림톡 요청 중 오류 발생: {}", error.getMessage()))
                    .doOnSuccess(response -> log.info("알림톡 초기 요청 성공"))
                    .doOnNext(this::check)
                    .then();
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생: {}", ex.getMessage());
            //todo : 추가 처리 디코 웹훅
            return Mono.empty();
        }
    }

    private void check(MessageResponse response) {
        if (response.code() != 1000) {
            log.error("전송실패 errorCode : {} errorMessage : {}", response.code(), response.description());
            throw new IllegalArgumentException(response.code() + ", " + response.description());
        }
        log.info("알림톡 요청에 성공하였습니다.");
    }

    public void checkByWebHook(MessageStatusRequest request) {
        if (request.RESULT().equals("7000")) {
            log.info("{} 에 대한 알림톡 전송 완료", request.PHONE());
            return;
        }
        log.error("{} 에 대한 알림톡 전송 실패, MessageKey : {} ResultCode : {}",  request.PHONE(), request.MSGID(), request.RESULT());
        //todo : 디스코드 웹훅
    }
}
