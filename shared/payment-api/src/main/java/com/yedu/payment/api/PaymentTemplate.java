package com.yedu.payment.api;

import com.yedu.common.webclient.WebClientProperties;
import com.yedu.payment.api.WebClientConfig.PaymentConfig;
import com.yedu.payment.api.dto.SendBillRequest;
import com.yedu.payment.api.dto.SendBillResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
@RequiredArgsConstructor
public class PaymentTemplate {

  private final WebClient paymentWebClient;

  public SendBillResponse sendBill(SendBillRequest request){
    return paymentWebClient.post()
        .uri("/bills")
        .bodyValue(request)
        .exchangeToMono(response -> response.bodyToMono(SendBillResponse.class))
        .block();
  }
}
