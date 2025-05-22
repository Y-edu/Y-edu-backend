package com.yedu.paymint.api;


import com.yedu.paymint.api.dto.SendBillRequest;
import com.yedu.paymint.api.dto.SendBillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class PaymintApi {

  private final WebClient paymintWebClient;

  /**
   * 청구서 발송 요청
   */
  public SendBillResponse sendBill(SendBillRequest request) {
    return paymintWebClient.post()
        .uri("/if/bill/send")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(SendBillResponse.class)
        .block();
  }


}
