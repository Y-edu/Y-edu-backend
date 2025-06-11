package com.yedu.bizppurio.support.application.usecase;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.dto.res.MessageResponse;
import com.yedu.cache.support.RedisRepository;
import java.time.Duration;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Slf4j
@Component
public class BizppurioApiTemplate {

  private static final String PHONE_REGEX = "^010\\d{8}$";

  private final BizppurioAuth bizppurioAuth;
  private final ObjectMapper objectMapper;
  private final WebClient webClient;
  private final RedisRepository redisRepository;

  @Value("${bizppurio.message}")
  private String messageUrl;

  public MessageResponse send(CommonRequest commonRequest) {
    if (!Pattern.matches(PHONE_REGEX, commonRequest.to())) {
      log.error("알림톡 발송 실패, 전화번호 오류 : {} / commonRequest : {}", commonRequest.to(), commonRequest);
      throw new IllegalArgumentException();
    }

    String request;
    String accessToken = bizppurioAuth.getAuth();
    try {
      request = objectMapper.writeValueAsString(commonRequest);
    } catch (Exception e) {
      log.error("Json 직렬화 실패");
      throw new IllegalArgumentException("Json 직렬화 실패");
    }
    log.info("알림톡 발송 : {}", request);
    String refkey = commonRequest.refkey();
    String message = commonRequest.content().getContent().getMessage();
    redisRepository.setValues(refkey, message, Duration.ofSeconds(30l));
    return webClient
        .post()
        .uri(messageUrl)
        .headers(h -> h.setContentType(APPLICATION_JSON))
        .headers(h -> h.setBearerAuth(accessToken))
        .bodyValue(request)
        .retrieve()
        .onStatus(
            HttpStatusCode::isError,
            response ->
                response
                    .bodyToMono(String.class)
                    .map(
                        errorBody ->
                            new IllegalArgumentException("비즈뿌리오 알림톡 전송 API 요청 실패: " + errorBody)))
        .bodyToMono(MessageResponse.class)
        .doOnSubscribe(subscription -> log.info("알림톡 요청 시작"))
        .doOnSuccess(response -> log.info("알림톡 초기 요청 성공"))
        .doOnError(error -> log.error("알림톡 초기 요청 실패 : {}", error.getMessage()))
        .block();
  }
}
