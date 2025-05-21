package com.yedu.api.domain.matching.application.dto.res;

import java.time.LocalDate;

public record RetrieveSessionDateResponse(LocalDate sessionDate, Boolean isComplete) {

  public static RetrieveSessionDateResponse empty() {
    return new RetrieveSessionDateResponse(null, false);
  }
}
