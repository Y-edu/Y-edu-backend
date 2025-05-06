package com.yedu.bizppurio.support.application.usecase;

import static com.yedu.bizppurio.support.event.mapper.DiscordEventMapper.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.constant.BizppurioResponseCode;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.cache.support.RedisRepository;
import com.yedu.common.dto.MessageStatusRequest;
import com.yedu.common.event.bizppurio.MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent;
import com.yedu.common.event.bizppurio.MatchingParentsEvent.ParentsClassNoticeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
  private final BizppurioMapper bizppurioMapper;
  private final BizppurioApiTemplate bizppurioApiTemplate;

  public void checkByWebHook(MessageStatusRequest request) {
    if (request.RESULT().equals(SUCCESS)) {
      log.info("{} 에 대한 알림톡 전송 완료", request.PHONE());
      checkNextStep(request);
      return;
    }
    failCase(request);
  }

  private void checkNextStep(MessageStatusRequest request) {
    if (redisRepository.getValues(NEXT + request.REFKEY()).isPresent()) {
      String[] splits = redisRepository.getValues(NEXT + request.REFKEY()).get().split("\\|", 2);
      String type = splits[0];
      String value = splits[1];
      switch (type) {
        case WRITE_FIN_TALK -> {
          try {
            IntroduceWriteFinishTalkEvent event =
                objectMapper.readValue(value, IntroduceWriteFinishTalkEvent.class);
            CommonRequest commonRequest = bizppurioMapper.mapToIntroduceWriteFinishTalk(event);
            bizppurioApiTemplate.send(commonRequest);
          } catch (JsonProcessingException e) {
            log.error("objectMapper 예외 발생");
            throw new RuntimeException(e);
          }
        }
        case CLASS_NOTICE -> {
          try {
            ParentsClassNoticeEvent event =
                objectMapper.readValue(value, ParentsClassNoticeEvent.class);
            CommonRequest commonRequest = bizppurioMapper.mapToParentsClassNotice(event);
            bizppurioApiTemplate.send(commonRequest);
          } catch (JsonProcessingException e) {
            log.error("objectMapper 예외 발생");
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  private void failCase(MessageStatusRequest request) {
    BizppurioResponseCode bizppurioResponseCode =
        BizppurioResponseCode.findByCode(Integer.parseInt(request.RESULT()))
            .orElseGet(() -> BizppurioResponseCode.NOTFOUND_ERROR);
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
    eventPublisher.publishEvent(
        mapToAlarmTalkErrorInfoEvent(request.PHONE(), message, String.valueOf(code), errorMessage));
  }
}
