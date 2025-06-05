package com.yedu.api.domain.matching.application.dto.req;

public record CompleteSessionTokenRequest(
    String token, Integer classMinute, String understanding, String homework) {}
