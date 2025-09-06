package com.yedu.payment.api.dto;

import java.math.BigDecimal;

public record SendBillRequest(
    String receiverName,
    String receiverPhoneNumber,
    String title,
    String message,
    BigDecimal price
) {}
