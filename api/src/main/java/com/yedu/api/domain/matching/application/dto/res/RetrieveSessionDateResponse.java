package com.yedu.api.domain.matching.application.dto.res;

import com.yedu.api.domain.matching.domain.vo.ClassTime;
import java.time.LocalDate;

public record RetrieveSessionDateResponse(LocalDate sessionDate, Boolean isComplete, Long teacherId, ClassTime classTime) {

  public static RetrieveSessionDateResponse empty() {
    return new RetrieveSessionDateResponse(null, false, null, null);
  }
}
