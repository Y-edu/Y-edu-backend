package com.yedu.discord.support;

import com.yedu.common.WebClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("discord")
record DiscordWebClientProperties(
    Webhook webhook,
    WebClientProperties webClientProperties
) {

  public record Webhook(
      String url,
      String teacher,
      String scheduleCancel
  ) {}
}
