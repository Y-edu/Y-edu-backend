package com.yedu.backend.domain.parents.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ClassType {
    수학("math"), 영어("english");

    private final String description;
}
