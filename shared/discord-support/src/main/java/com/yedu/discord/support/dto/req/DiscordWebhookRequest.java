package com.yedu.discord.support.dto.req;

import java.util.List;

public record DiscordWebhookRequest(List<Embed> embeds) {

  public record Embed(String title, String description, int color, List<Field> fields, Footer footer) {}

  public record Field(String name, String value) {}

  public record Footer(String text) {}

}
