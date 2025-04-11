package com.yedu.discord.support;

import com.yedu.discord.support.dto.req.DiscordColor;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest.Embed;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest.Field;
import com.yedu.discord.support.dto.req.DiscordWebhookRequest.Footer;
import java.time.LocalDateTime;
import java.util.List;

public class DiscordMapper {

    public static DiscordWebhookRequest mapToDiscordWithServerAlarm(String title, String description, List<Field> fields) {
        return new DiscordWebhookRequest(
                List.of(new Embed(title, description, DiscordColor.RED.getCode(), fields, new Footer(LocalDateTime.now().toString())))
        );
    }

    public static DiscordWebhookRequest mapToDiscordWithInformation(String title, List<Field> fields) {
        return new DiscordWebhookRequest(
                List.of(new Embed(title, null, DiscordColor.LIGHT_BLUE.getCode(), fields, new Footer(null)))
        );
    }

    public static Field mapToField(String name, String value) {
        return new Field(name, value);
    }
}
