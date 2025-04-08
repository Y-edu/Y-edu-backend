package com.yedu.backend.global.config.security.jwt.dto;

public record JwtResponse(int accessTokenExpired, int refreshTokenExpired) {
}
