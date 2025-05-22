package com.yedu.paymint.api.dto;

public record RetrieveBillPaymentStatusResponse(
    String apikey,                       // ERP 연동코드
    String bill_id,                      // 청구서 ID
    String appr_cat_id,                  // cat id
    String appr_pay_type,                // 결제수단
    String appr_card_type,              // 결제카드 종류
    String appr_dt,                      // 승인일시 (YYYYMMDDHHMMSS)
    String appr_origin_dt,              // 원거래 승인일시 (취소 거래 시 사용)
    String appr_price,                   // 승인금액
    String appr_issuer,                  // 결제은행 / 카드명
    String appr_issuer_cd,               // 발행카드코드 / 은행코드
    String appr_issuer_num,              // 결제카드 / 계좌번호
    String appr_acquirer_cd,             // 매입사코드
    String appr_acquirer_nm,             // 매입사명
    String appr_num,                     // 승인번호
    String appr_origin_num,              // 원거래 승인번호 (취소 거래 시 사용)
    String appr_res_cd,                  // 응답코드
    String appr_monthly,                 // 할부개월수
    String appr_state,                   // 결제 상태 (F:완료, W:미결제, C:취소, D:파기)
    String appr_cash_num,                // 현금영수증 발급 승인번호
    String appr_cash_trader,             // 현금영수증 발급 구분 (개인:0, 사업자:1)
    String appr_cash_issuance_number     // 현금영수증 발급 요청 번호
) {}
