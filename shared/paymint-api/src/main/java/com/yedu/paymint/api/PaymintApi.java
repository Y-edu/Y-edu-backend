package com.yedu.paymint.api;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class PaymintApi {

  private final WebClient paymintWebClient;

}
