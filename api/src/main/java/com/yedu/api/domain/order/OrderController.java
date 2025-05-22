package com.yedu.api.domain.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class OrderController {

  /**
   * 승인동기화
   */
  @PostMapping("/order/result")
  void orderResult(){

  }
}
