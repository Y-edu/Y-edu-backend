package com.yedu.common.event.discord;

import java.time.LocalDateTime;

public record NotificationDeliverySuccessEvent(
    String senderProfileDesc,
    String templateDescription,
    String receiverTypeDesc,
    String receiverPhoneNumber,
    String content,
    LocalDateTime deliveredAt
) {}
