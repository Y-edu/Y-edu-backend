package com.yedu.paymint.api.dto;

public record DestroyBillRequest(
    String apikey,     // ERP 연동코드
    String member,     // 가맹점 사용자 코드
    String merchant,   // 가맹점 매장 코드
    String bill_id,    // 청구서 ID
    String price,      // 청구금액
    String hash        // SHA-256 hash: bill_id + "," + price
){

}
