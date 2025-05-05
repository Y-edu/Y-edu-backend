package com.yedu.api.domain.parents.application.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertFalse;

@Schema(description = "과외 타임 테이블 요청 객체")
public record ApplicationFormTimeTableRequest(
    @Schema(description = "알림톡에 발급된 토큰", example = "eqew-2wefq-qwfeq-ebrre") String token,
    @Schema(example = "온라인11a") String applicationFormId) {

  @AssertFalse(message = "token 또는 applicationFormId 중 하나는 입력되야 합니다")
  @JsonIgnore
  public boolean isInvalidRequest() {
    return StringUtils.isEmpty(token) && StringUtils.isEmpty(applicationFormId);
  }
}
