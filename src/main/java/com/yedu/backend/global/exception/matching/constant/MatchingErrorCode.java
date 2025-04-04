package com.yedu.backend.global.exception.matching.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MatchingErrorCode {
    NOTFOUND_INFO("EX201", "해당하는 매칭을 찾을 수 없습니다 - applicationFormId : %s teacherId : %s phoneNumber : %s"),
    NOT_WAITING("EX202", "매칭이 대기 상태가 아닙니다 - classMatchingId : %s"),
    NOTFOUND_INFO_WITH_MATCHING_ID("EX203", "해당하는 매칭을 찾을 수 없습니다 - matchingId : %s"),
    ;

    private final String code;
    private final String message;


    public String formatMessage(String... args){
        return message.formatted(args);
    }
}
