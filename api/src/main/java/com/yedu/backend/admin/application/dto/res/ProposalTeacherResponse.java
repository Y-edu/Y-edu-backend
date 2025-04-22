package com.yedu.backend.admin.application.dto.res;

import java.util.List;

public record ProposalTeacherResponse(List<TokenResponse> tokens) {

  public record TokenResponse(
      Long teacherId, String applicationFormId, String phoneNumber, String token) {}
}
