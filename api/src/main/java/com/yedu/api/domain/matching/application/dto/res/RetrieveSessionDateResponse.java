package com.yedu.api.domain.matching.application.dto.res;

import java.time.LocalDate;

public record RetrieveSessionDateResponse(LocalDate sessionDate, Boolean isComplete, Long teacherId) {

  public static RetrieveSessionDateResponse empty() {
    return new RetrieveSessionDateResponse(null, false, null);
  }
}
