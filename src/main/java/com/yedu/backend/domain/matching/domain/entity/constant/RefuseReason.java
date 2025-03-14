package com.yedu.backend.domain.matching.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RefuseReason {
    UNABLE_NOW("지금은 수업이 불가해요"),
    UNABLE_DISTRICT("가능한 지역이 아니에요");

    private final String reason;
}
