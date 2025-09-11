package com.yedu.api.domain.matching.application.usecase;

import com.yedu.api.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.api.domain.matching.domain.service.ClassSessionCommandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentUseCase {

  private final ClassScheduleMatchingUseCase classScheduleMatchingUseCase;


  private final ClassSessionCommandService sessionCommandService;

  public void approve(Long matchingId) {
    // 입금 단계 생략 후 바로 매칭 진행
    classScheduleMatchingUseCase.schedule(new ClassScheduleMatchingRequest(matchingId));
  }

  public void approveSessions(List<Long> sessionIds) {
    sessionCommandService.pay(sessionIds);
  }
}
