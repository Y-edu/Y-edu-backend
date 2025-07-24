package com.yedu.api.domain.matching.application.dto.req;

import com.yedu.api.domain.matching.domain.entity.constant.CancelReason;

public record CancelSessionRequest(CancelReason cancelReason, Boolean isTodayCancel) {
  public CancelSessionRequest {
    if (isTodayCancel == null) {
      isTodayCancel = false;
    }
  }
}
