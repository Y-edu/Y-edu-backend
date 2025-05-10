package com.yedu.api.domain.matching.application.dto.req;

public record CompleteSessionTokenRequest(
    String token, String understanding, Integer homeworkPercentage) {}
