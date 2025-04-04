package com.yedu.backend.global.event.dto;

public record InviteMatchingChannelInfoEvent(
        String name,
        String nickName,
        String phoneNumber
) {
}
