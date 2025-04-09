package com.yedu.discord.support;

import com.yedu.common.WebClientProperties;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("discord")
record DiscordWebClientProperties(
    Map<DiscordWebhookType, String> webhooks,
    WebClientProperties webClientProperties
) {

  public String resolveUrl(DiscordWebhookType webhookType) {
    return webhooks.get(webhookType);
  }
}
