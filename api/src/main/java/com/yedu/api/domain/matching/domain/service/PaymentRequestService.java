package com.yedu.api.domain.matching.domain.service;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentRequestService {
    
    private final WebClient paymentRequestWebClient;

    @Value("${app.payment-request-api.url}")
    private String paymentUrl;
    
    public void requestPayment(Long classSessionId) {        
        log.info("결제 요청 전송 - URL: {}, classSessionId: {}", paymentUrl, classSessionId);
        
        try {
            paymentRequestWebClient.post()
                    .uri(paymentUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("classSessionId", classSessionId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> log.info("결제 요청 성공 - classSessionId: {}, 응답: {}", classSessionId, response))
                    .doOnError(error -> log.error("결제 요청 실패 - classSessionId: {}, 오류: {}", classSessionId, error.getMessage()))
                    .subscribe();
        } catch (Exception e) {
            log.error("결제 요청 중 예외 발생 - classSessionId: {}, 오류: {}", classSessionId, e.getMessage(), e);
        }
    }
}
