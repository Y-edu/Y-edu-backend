package com.yedu.common.event.bizppurio;

public record MatchingAcceptCaseInfoEvent(
    String online,
    String classType,
    String district,
    String dong,
    String age,
    String phoneNumber) {}
