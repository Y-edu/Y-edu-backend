package com.yedu.rabbitmq.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq")
public record RabbitMqProperties(
    String exchange, QueueProperties parent, QueueProperties teacher, QueueProperties system) {

  public record QueueProperties(String queue, String routingKey) {}
}
