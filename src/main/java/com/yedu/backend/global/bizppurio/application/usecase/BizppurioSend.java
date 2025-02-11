package com.yedu.backend.global.bizppurio.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.backend.global.bizppurio.application.dto.req.CommonRequest;
import com.yedu.backend.global.bizppurio.application.dto.res.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
            log.info("알림톡 발송 : {}", messageSupplier.get().to());
            CommonRequest commonRequest = messageSupplier.get();
            String accessToken = bizppurioAuth.getAuth();
            String request = objectMapper.writeValueAsString(commonRequest);
            return webClient.post()
                    .uri(messageUrl)
                    .headers(h -> h.setContentType(APPLICATION_JSON))
                    .headers(h -> h.setBearerAuth(accessToken))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(MessageResponse.class)
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
        log.info("알림톡 전송에 성공하였습니다. : {} {}", response.code(), response.description());
    }
}
