package com.yedu.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.bizppurio.support.application.dto.req.CommonRequest;
import com.yedu.bizppurio.support.application.dto.res.MessageResponse;
import com.yedu.bizppurio.support.application.usecase.BizppurioApiTemplate;
import com.yedu.consumer.domain.notification.entity.Notification;
import com.yedu.consumer.domain.notification.repository.NotificationRepository;
import com.yedu.rabbitmq.support.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractConsumer implements Consumer<Message> {

  @Value("${message.enable}")
  private Boolean enable;

  private final Set<String> testerPhoneNumbers = Set.of("01059367332");

  protected final Map<Class<?>, Function<Message, CommonRequest>> parsers = new HashMap<>();

  private final BizppurioApiTemplate apiTemplate;

  protected final NotificationRepository notificationRepository;

  protected final ObjectMapper objectMapper;

  public abstract Notification beforeConsume(CommonRequest commonRequest);

  @Transactional
  @Override
  public void accept(Message message) {
    CommonRequest commonRequest = parsers.get(message.type()).apply(message);
    Notification notification = beforeConsume(commonRequest);

    if (!enable && !testerPhoneNumbers.contains(notification.getReceiverPhoneNumber())) {
       throw new IllegalStateException("메시지 처리가 비활성화되어 있습니다.");
    }

    notificationRepository.save(notification);
    try {
      MessageResponse response = apiTemplate.send(commonRequest);
      notification.success(response.messagekey());
    } catch (Exception e) {
      notification.fail();
      log.error("메시지 처리 중 예외 발생 - 타입: {}, 오류: {}", message.type(), e.getMessage(), e);
    }
  }

  protected <T> T convert(Message message, Class<T> clazz) {
    return objectMapper.convertValue(message.data(), clazz);
  }

  protected <T> void registerParser(Class<T> clazz, Function<T, CommonRequest> mapperFunc) {
    parsers.put(
        clazz,
        msg -> {
          T convert = convert(msg, clazz);
          return mapperFunc.apply(convert);
        });
  }
}
