package com.yedu.rabbitmq.support;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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
  public Queue parentQueue() {
    return QueueBuilder.durable(properties.parent().queue()).build();
  }

  @Bean
  public Queue teacherQueue() {
    return QueueBuilder.durable(properties.teacher().queue()).build();
  }

  @Bean
  public Queue systemQueue() {
    return QueueBuilder.durable(properties.system().queue())
        .withArgument("x-max-priority", 10)
        .build();
  }

  @Bean
  public Binding parentBinding(Queue parentQueue, DirectExchange exchange) {
    return BindingBuilder.bind(parentQueue).to(exchange).with(properties.parent().routingKey());
  }

  @Bean
  public Binding teacherBinding(Queue teacherQueue, DirectExchange exchange) {
    return BindingBuilder.bind(teacherQueue).to(exchange).with(properties.teacher().routingKey());
  }

  @Bean
  public Binding systemBinding(Queue systemQueue, DirectExchange exchange) {
    return BindingBuilder.bind(systemQueue).to(exchange).with(properties.system().routingKey());
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
