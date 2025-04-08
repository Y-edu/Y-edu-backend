package com.yedu.backend.global.config.security.jwt.usecase;

import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.global.config.security.jwt.constant.Role;
import com.yedu.backend.global.config.security.jwt.dto.JwtResponse;
import com.yedu.backend.global.config.security.jwt.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class JwtUseCase {
    private final JwtUtils jwtUtils;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    public JwtResponse signIn(Admin admin, HttpServletResponse response) {
        return generateAdminToken(admin, response);
    }

    public void logout(Admin admin) {
        jwtUtils.makeExpired(admin.getAdminId());
    }

    public JwtResponse regenerateToken(Admin admin, HttpServletRequest request, HttpServletResponse response) {
        jwtUtils.checkRedis(admin.getAdminId(), request);
        generateAdminToken(admin, response);
        return new JwtResponse(accessExpiration, refreshExpiration);
    }


    private JwtResponse generateAdminToken(Admin admin, HttpServletResponse response) {
        String accessToken = jwtUtils.generateAccessToken(admin.getAdminId(), Role.ADMIN);
        String refreshToken = jwtUtils.generateRefreshToken(admin.getAdminId(), Role.ADMIN);
        accessTokenHeader(response, accessToken);
        refreshTokenHeader(response, refreshToken);
        return new JwtResponse(accessExpiration, refreshExpiration);
    }

    // 헤더에 AccessToken 추가
    private void accessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    // 헤더에 RefreshToken 추가
    private void refreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader("RefreshToken", refreshToken);
    }
}
