package com.yedu.paymint.api.dto;

public record RetrieveBillPaymentStatusRequest(
    String apikey,     // ERP 연동코드
    String member,     // 가맹점 사용자 코드
    String merchant,   // 가맹점 매장 코드
    String bill_id     // 청구서 ID
) {}
