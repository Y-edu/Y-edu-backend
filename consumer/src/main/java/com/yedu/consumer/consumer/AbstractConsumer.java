package com.yedu.consumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedu.rabbitmq.support.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractConsumer implements Consumer<Message> {

  protected final Map<Class<?>, Consumer<Message>> handlers = new HashMap<>();

  protected final ObjectMapper objectMapper;

  @SneakyThrows
  @Override
  public void accept(Message message) {
    Consumer<Message> handler = handlers.get(message.type());
    if (handler != null) {
      handler.accept(message);
    } else {
      log.warn("Unhandled message type: {}", message.type());
    }
    afterConsume(message);
  }

  protected  <T> T convert(Message message, Class<T> clazz) {
    return objectMapper.convertValue(message.data(), clazz);
  }

  abstract void afterConsume(Message message);
}
