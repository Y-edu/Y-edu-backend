package com.yedu.backend.global.discord;

import java.util.List;

public record DiscordWebhookRequest(List<Embed> embeds) {}
record Embed(String title, String description, int color, List<Field> fields, Footer footer) {}
record Field(String name, String value) {}
record Footer(String text) {}
