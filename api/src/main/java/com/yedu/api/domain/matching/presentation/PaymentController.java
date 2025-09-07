package com.yedu.api.domain.matching.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "PAYMENT Controller")
public class PaymentController {

  @PostMapping("/matchings/{matchingId}/pay")
  @Operation(hidden = true)
  public ResponseEntity<?> approve(@PathVariable Long matchingId) {
      log.info(">>> matchingId: {} 결제 승인 완료", matchingId);

      return ResponseEntity.noContent().build();
  }

}
