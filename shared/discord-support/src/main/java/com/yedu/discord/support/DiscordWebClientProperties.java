package com.yedu.discord.support;

import com.yedu.common.webclient.WebClientProperties;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("discord")
record DiscordWebClientProperties(
    Map<DiscordWebhookType, String> webhooks,
    WebClientProperties webClientProperties,
    Integer retryCount,
    Integer retryIntervalSeconds
) {

  public String resolveUrl(DiscordWebhookType webhookType) {
    return webhooks.get(webhookType);
  }
}
