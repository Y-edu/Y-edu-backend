package com.yedu.backend.global.discord;

import java.time.LocalDateTime;
import java.util.List;

public class DiscordMapper {
    public static DiscordWebhookRequest mapToDiscordWebhook(String title, String description, List<Field> fields) {
        return new DiscordWebhookRequest(
            List.of(new Embed(title, description, 16711680, fields, new Footer(LocalDateTime.now().toString())))
        );
    }

    public static Field mapToField(String name, String value) {
        return new Field(name, value);
    }
}
