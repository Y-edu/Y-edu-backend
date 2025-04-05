package com.yedu.backend.global.event.dto;

public record MatchingConfirmTeacherEvent(ClassGuideEvent classGuideEvent, IntroduceFinishTalkEvent introduceFinishTalkEvent, IntroduceWriteFinishTalkEvent introduceWriteFinishTalkEvent) {
    public record ClassGuideEvent(
            String phoneNumber
    ) {}
    public record IntroduceFinishTalkEvent(
            String phoneNumber
    ) {}
    public record IntroduceWriteFinishTalkEvent(
            String applicationFormId,
            int count,
            int time,
            String phoneNumber
    ) {}

}
