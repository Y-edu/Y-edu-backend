package com.yedu.api.domain.order;

import com.yedu.paymint.api.dto.ApproveBillRequest;
import com.yedu.paymint.api.dto.ApproveBillResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  /**
   * 승인동기화
   */
  @PostMapping("/order/result")
  public ApproveBillResponse approveBillWebHook(ApproveBillRequest request){
    return null;
  }
}
