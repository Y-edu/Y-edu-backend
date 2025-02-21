package com.yedu.backend.global.bizppurio.application.usecase;

import com.yedu.backend.global.bizppurio.application.dto.req.MessageStatusRequest;
import com.yedu.backend.global.config.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BizppurioCheckStep {
    private final RedisRepository redisRepository;
    private final BizppurioTeacherMessage teacherMessage;

    private static final String COUNSEL = "COUNSEL";

    public void checkNextStep(MessageStatusRequest request) {
        getCacheValue(request.REFKEY()).ifPresent(stepValue -> processNextStep(stepValue, request));
    }

    private Optional<String> getCacheValue(String refKey) {
        return redisRepository.getValues(refKey);
    }

    private void processNextStep(String stepValue, MessageStatusRequest request) {
        if (COUNSEL.equals(stepValue)) {
            teacherMessage.photoSubmit(request.PHONE());
            redisRepository.deleteValues(request.REFKEY());
        }
    }
}