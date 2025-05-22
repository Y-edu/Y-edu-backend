package com.yedu.api.domain.order;

import com.yedu.paymint.api.dto.BillApprovalRequest;
import com.yedu.paymint.api.dto.BillApprovalResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  /**
   * 승인동기화
   */
  @PostMapping("/order/result")
  public BillApprovalResponse approveBillWebHook(BillApprovalRequest request){
    return null;
  }
}
