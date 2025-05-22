package com.yedu.paymint.api.dto;

public record SendBillResponse(
    String apikey,      // 요청 시 보낸 apiKey 값
    String member,      // 요청 시 보낸 member 값
    String merchant,    // 요청 시 보낸 merchant 값
    String bill_id,     // 요청 시 보낸 bill_id 값
    String hash,        // 요청 시 보낸 hash 값
    String shortURL,    // 생성된 청구서 Short URL
    String code,        // 결과 코드 (성공/실패 여부를 나타내는 4자리 코드)
    String msg          // 결과 메시지
) {}
