package com.yedu.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ClassType {
    수학("math"), 영어("english");

    private final String description;
}
