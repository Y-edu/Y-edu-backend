package com.yedu.backend.global.event.dto;

public record MatchingParentsEvent(ParentsExchangeEvent parentsExchangeEvent, ParentsClassNoticeEvent parentsClassNoticeEvent) {
    public record ParentsExchangeEvent(
            String nickName,
            String teacherPhoneNumber,
            String parentsPhoneNumber
    ) {
    }
    public record ParentsClassNoticeEvent(String phoneNumber) {
    }
}
