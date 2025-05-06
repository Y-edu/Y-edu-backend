package com.yedu.rabbitmq.support;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfig {

  private final RabbitMqProperties properties;

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(properties.exchange());
  }

  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange(properties.exchange() + ".dlx");
  }

  @Bean
  public Queue parentQueue() {
    return QueueBuilder.durable(properties.parent().queue())
        .withArgument("x-dead-letter-exchange", properties.exchange() + ".dlx")
        .withArgument("x-dead-letter-routing-key", properties.parent().queue() + ".dlq")
        .build();
  }

  @Bean
  public Queue parentDlq() {
    return QueueBuilder.durable(properties.parent().queue() + ".dlq").build();
  }

  @Bean
  public Binding parentBinding(Queue parentQueue, DirectExchange exchange) {
    return BindingBuilder.bind(parentQueue).to(exchange).with(properties.parent().routingKey());
  }

  @Bean
  public Binding parentDlqBinding(Queue parentDlq, DirectExchange deadLetterExchange) {
    return BindingBuilder.bind(parentDlq)
        .to(deadLetterExchange)
        .with(properties.parent().queue() + ".dlq");
  }

  // teacher 큐 및 DLQ
  @Bean
  public Queue teacherQueue() {
    return QueueBuilder.durable(properties.teacher().queue())
        .withArgument("x-dead-letter-exchange", properties.exchange() + ".dlx")
        .withArgument("x-dead-letter-routing-key", properties.teacher().queue() + ".dlq")
        .build();
  }

  @Bean
  public Queue teacherDlq() {
    return QueueBuilder.durable(properties.teacher().queue() + ".dlq").build();
  }

  @Bean
  public Binding teacherBinding(Queue teacherQueue, DirectExchange exchange) {
    return BindingBuilder.bind(teacherQueue).to(exchange).with(properties.teacher().routingKey());
  }

  @Bean
  public Binding teacherDlqBinding(Queue teacherDlq, DirectExchange deadLetterExchange) {
    return BindingBuilder.bind(teacherDlq)
        .to(deadLetterExchange)
        .with(properties.teacher().queue() + ".dlq");
  }

  @Bean
  public Queue systemQueue() {
    return QueueBuilder.durable(properties.system().queue())
        .withArgument("x-max-priority", 10)
        .withArgument("x-dead-letter-exchange", properties.exchange() + ".dlx")
        .withArgument("x-dead-letter-routing-key", properties.system().queue() + ".dlq")
        .build();
  }

  @Bean
  public Queue systemDlq() {
    return QueueBuilder.durable(properties.system().queue() + ".dlq").build();
  }

  @Bean
  public Binding systemBinding(Queue systemQueue, DirectExchange exchange) {
    return BindingBuilder.bind(systemQueue).to(exchange).with(properties.system().routingKey());
  }

  @Bean
  public Binding systemDlqBinding(Queue systemDlq, DirectExchange deadLetterExchange) {
    return BindingBuilder.bind(systemDlq)
        .to(deadLetterExchange)
        .with(properties.system().queue() + ".dlq");
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter);
    return template;
  }
}
