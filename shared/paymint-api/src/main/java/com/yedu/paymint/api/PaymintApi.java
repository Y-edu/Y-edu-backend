package com.yedu.paymint.api;


import com.yedu.paymint.api.dto.DestroyBillRequest;
import com.yedu.paymint.api.dto.DestroyBillResponse;
import com.yedu.paymint.api.dto.CancelPaymentRequest;
import com.yedu.paymint.api.dto.CancelPaymentResponse;
import com.yedu.paymint.api.dto.RetrieveBillPaymentStatusRequest;
import com.yedu.paymint.api.dto.RetrieveBillPaymentStatusResponse;
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

  /**
   * 결제 취소
   */
  public CancelPaymentResponse cancelPayment(CancelPaymentRequest request) {
    return paymintWebClient.post()
        .uri("/if/bill/cancel")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(CancelPaymentResponse.class)
        .block();
  }

  /**
   * 청구서 파기
   */
  public DestroyBillResponse destroyBill(DestroyBillRequest request) {
    return paymintWebClient.post()
        .uri("/if/bill/destroy")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(DestroyBillResponse.class)
        .block();
  }


  /**
   * 결제 상태 조회
   */
  public RetrieveBillPaymentStatusResponse retrieveBillPaymentStatus(RetrieveBillPaymentStatusRequest request) {
    return paymintWebClient.post()
        .uri("/if/bill/read")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(RetrieveBillPaymentStatusResponse.class)
        .block();
  }


}
