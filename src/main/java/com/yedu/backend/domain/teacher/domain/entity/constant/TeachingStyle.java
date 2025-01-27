package com.yedu.backend.domain.teacher.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Getter
@Slf4j
public enum TeachingStyle {
    CARING("따뜻하고 친절한 선생님"),
    PASSIONATE("열정적인 선생님"),
    METICULOUS("꼼꼼한 선생님"),
    FUN("재미있는 선생님"),
    CONFIDENCE_BOOSTER("자존감 지킴이 선생님"),
    FOCUSED("수업 중심을 잡아주는 능숙한 선생님"),
    ERROR("선택에 없는 예시 전달");

    private final String description;

    public static TeachingStyle fromString(String teachingStyle){
        for (TeachingStyle style : values()) {
            if (teachingStyle.equals(style.description))
                return style;
        }
        log.error("TeachingStyle 처리 예외 발생");
        return ERROR;
    }
}

