package com.yedu.consumer.listener;

import com.yedu.consumer.consumer.AbstractConsumer;
import com.yedu.consumer.consumer.ParentMessageConsumer;
import com.yedu.consumer.consumer.SystemMessageConsumer;
import com.yedu.consumer.consumer.TeacherMessageConsumer;
import com.yedu.rabbitmq.support.Message;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitListenerService {

  private final AbstractConsumer parentMessageConsumer;
  private final AbstractConsumer teacherMessageConsumer;
  private final SystemMessageConsumer systemMessageConsumer;

  @SneakyThrows
  @RabbitListener(queues = "${rabbitmq.parent.queue}")
  public void consumeParentMessage(Message message) {
    parentMessageConsumer.accept(message);
  }

  @SneakyThrows
  @RabbitListener(queues = "${rabbitmq.teacher.queue}")
  public void consumeTeacherMessage(Message message) {
    teacherMessageConsumer.accept(message);
  }

  @SneakyThrows
  @RabbitListener(queues = "${rabbitmq.system.queue}")
  public void consumeSystemMessage(Message message) {
    systemMessageConsumer.accept(message);
  }
}
