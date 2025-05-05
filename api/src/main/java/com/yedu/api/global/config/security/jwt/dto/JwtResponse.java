package com.yedu.api.global.config.security.jwt.dto;

public record JwtResponse(int accessTokenExpired, int refreshTokenExpired) {}
