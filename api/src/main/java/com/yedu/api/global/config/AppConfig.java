package com.yedu.api.global.config;

import com.yedu.payment.PaymentOperation;
import com.yedu.payment.PaymentOperationService;
import com.yedu.payment.PaymentOperationWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

  @Bean
  public PaymentOperationWrapper paymentOperationWrapper(){
    return new PaymentOperationWrapper(paymentOperation());
  }

  @Bean
  public PaymentOperation paymentOperation() {
    return new PaymentOperationService(paymentWebClient());
  }

  @Bean
  public WebClient paymentWebClient() {
    return WebClient.builder()
        .build();
  }
}
