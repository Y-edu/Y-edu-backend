package com.yedu.backend.bizppurio.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.backend.bizppurio.application.constant.BizppurioResponseCode;
import com.yedu.backend.bizppurio.application.dto.req.MessageStatusRequest;
import com.yedu.backend.global.config.redis.RedisRepository;
import com.yedu.backend.global.discord.DiscordWebhookUseCase;
import com.yedu.backend.global.event.dto.MatchingConfirmTeacherEvent.IntroduceWriteFinishTalkEvent;
import com.yedu.backend.global.event.dto.MatchingParentsEvent.ParentsClassNoticeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final DiscordWebhookUseCase discordWebhookUseCase;
    private final ObjectMapper objectMapper;
    private final BizppurioTeacherMessage teacherMessage;
    private final BizppurioParentsMessage parentsMessage;

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
                        IntroduceWriteFinishTalkEvent event = objectMapper.readValue(value, IntroduceWriteFinishTalkEvent.class);
                        teacherMessage.introduceWriteFinishTalk(event);
                    } catch (JsonProcessingException e) {
                        log.error("objectMapper 예외 발생");
                        throw new RuntimeException(e);
                    }
                }
                case CLASS_NOTICE -> {
                    try {
                        ParentsClassNoticeEvent event = objectMapper.readValue(value, ParentsClassNoticeEvent.class);
                        parentsMessage.parentsClassNotice(event);
                    } catch (JsonProcessingException e) {
                        log.error("objectMapper 예외 발생");
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void failCase(MessageStatusRequest request) {
        BizppurioResponseCode bizppurioResponseCode = BizppurioResponseCode.findByCode(Integer.parseInt(request.RESULT()))
                .orElseGet(() -> BizppurioResponseCode.NOTFOUND_ERROR);
        String errorMessage = bizppurioResponseCode.getMessage();
        int code = bizppurioResponseCode.getCode();
        String message = redisRepository.getValues(request.REFKEY()).orElse("내용 알 수 없음");
        log.error("{} 에 대한 알림톡 전송 실패, 내용 {} \nRefKey : {} ResultCode : {} {}",  request.PHONE(), message, request.REFKEY(), code, errorMessage);
        discordWebhookUseCase.sendAlarmTalkError(request.PHONE(), message, String.valueOf(code), errorMessage);
    }
}