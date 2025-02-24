package com.yedu.backend.global.discord;

import java.time.LocalDateTime;
import java.util.List;

public class DiscordMapper {

    public static DiscordWebhookRequest mapToDiscordWithServerAlarm(String title, String description, List<Field> fields) {
        return new DiscordWebhookRequest(
                List.of(new Embed(title, description, DiscordColor.RED.getCode(), fields, new Footer(LocalDateTime.now().toString())))
        );
    }

    public static DiscordWebhookRequest mapToDiscordWithTeacherAlarm(String title, List<Field> fields) {
        return new DiscordWebhookRequest(
                List.of(new Embed(title, null, DiscordColor.LIGHT_BLUE.getCode(), fields, new Footer(null)))
        );
    }

    public static Field mapToField(String name, String value) {
        return new Field(name, value);
    }
}
