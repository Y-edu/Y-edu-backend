package com.yedu.paymint.api.dto;

public record CancelPaymentResponse(
    String apikey,             // ERP 연동코드
    String member,             // 가맹점 사용자 코드
    String merchant,           // 가맹점 매장 코드
    String bill_id,            // 청구서 ID
    String hash,               // 요청 시 보낸 SHA-256 해시 값
    String appr_num,           // 취소 시 거래번호
    String appr_origin_num,    // 원거래 승인번호
    String appr_cancel_dt,     // 취소일시 (YYYYMMDDHHMMSS)
    String code,               // 결과 코드 (4자리)
    String msg                 // 결과 메시지
) {}
