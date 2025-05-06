package com.yedu.bizppurio.support.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.constant.BizppurioResponseCode;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.dto.MessageStatusRequest;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent;
import com.yedu.common.event.bizppurio.MatchingParentsEvent.ParentsClassNoticeEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BizppurioCheckStep {

  private static final String SUCCESS = "7000";
  private static final String NEXT = "NEXT";
  private static final String WRITE_FIN_TALK = "WRITE_FIN_TALK";
  private static final String CLASS_NOTICE = "CLASS_NOTICE";
  private final RedisRepository redisRepository;
  private final ObjectMapper objectMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Async
  public void checkByWebHook(MessageStatusRequest request) {
    log.info("webhook 수신 : {}", request);
    if (isSuccess(request)) {
      log.info("{} 에 대한 알림톡 전송 완료", request.PHONE());
      checkNextStep(request);
      return;
    }
    failCase(request);
  }

  @SneakyThrows
  private void checkNextStep(MessageStatusRequest request) {
    if (redisRepository.getValues(NEXT + request.REFKEY()).isPresent()) {
      String[] splits = redisRepository.getValues(NEXT + request.REFKEY()).get().split("\\|", 2);
      String type = splits[0];
      String value = splits[1];
      switch (type) {
        case WRITE_FIN_TALK -> {
          IntroduceWriteFinishTalkEvent event =
              objectMapper.readValue(value, IntroduceWriteFinishTalkEvent.class);
          eventPublisher.publishEvent(event);
        }
        case CLASS_NOTICE -> {
          ParentsClassNoticeEvent event =
              objectMapper.readValue(value, ParentsClassNoticeEvent.class);
          eventPublisher.publishEvent(event);
        }
      }
    }
  }

  public boolean isSuccess(MessageStatusRequest request) {
    return request.RESULT().equals(SUCCESS);
  }

  private void failCase(MessageStatusRequest request) {
    BizppurioResponseCode bizppurioResponseCode =
        BizppurioResponseCode.findByCode(Integer.parseInt(request.RESULT()))
            .orElse(BizppurioResponseCode.NOTFOUND_ERROR);
    String errorMessage = bizppurioResponseCode.getMessage();
    int code = bizppurioResponseCode.getCode();
    String message = redisRepository.getValues(request.REFKEY()).orElse("내용 알 수 없음");
    log.error(
        "{} 에 대한 알림톡 전송 실패, 내용 {} \nRefKey : {} ResultCode : {} {}",
        request.PHONE(),
        message,
        request.REFKEY(),
        code,
        errorMessage);
  }
}
