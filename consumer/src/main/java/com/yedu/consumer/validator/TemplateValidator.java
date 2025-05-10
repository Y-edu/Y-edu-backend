package com.yedu.consumer.validator;

import com.yedu.bizppurio.support.application.mapper.BizppurioMapper;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.common.event.bizppurio.RecommendTeacherEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "message", name = "enable", havingValue = "false")
@Component
@RequiredArgsConstructor
public class TemplateValidator {

  private final BizppurioMapper mapper;

  private final BizppurioApiTemplate apiTemplate;

  private static final String TESTER_PHONE_NUMBER = "01059367332";

  /***
   * 알림톡 연동 validator
   * 템플릿 불일치 문제로 알림톡 발송 실패 방지
   */
  @PostConstruct
  void validate() {
    apiTemplate.send(
        mapper.mapToRecommendTeacher(
            new RecommendTeacherEvent(TESTER_PHONE_NUMBER, "테스트", "테스트", "테스트", 1, "test")));
  }
}
