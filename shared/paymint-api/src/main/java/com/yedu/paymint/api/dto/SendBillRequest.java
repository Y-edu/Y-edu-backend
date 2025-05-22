package com.yedu.paymint.api.dto;

public record SendBillRequest(
    String apikey,
    String member,
    String merchant,
    Bill bill
) {
  public record Bill(
      String bill_id,       // 청구서 ID (사업자번호 + 10자리 자유)
      String product_nm,    // 청구 사유
      String message,       // 안내 메시지
      String member_nm,     // 고객명
      String phone,         // 고객 전화번호 (숫자만, 11자리)
      String price,         // 결제 금액 (숫자만, 최대 10자리)
      String hash,          // SHA-256 해시
      String expire_dt,     // 유효기간 (YYYY-MM-DD)
      String callbackURL    // 결제 결과 수신 URL
  ) {}
}
