package com.yedu.paymint.api.dto;

public record DestroyBillResponse(
    String apikey, // ERP 연동코드
    String member, // 요청에 포함된 가맹점 사용자 코드
    String merchant, // 요청에 포함된 가맹점 매장 코드
    String bill_id, // 요청에 포함된 청구서 ID
    String hash, // 요청에 포함된 SHA-256 해시 값
    String code, // 결과 코드 (4자리)
    String msg // 결과 메시지
    ) {}
