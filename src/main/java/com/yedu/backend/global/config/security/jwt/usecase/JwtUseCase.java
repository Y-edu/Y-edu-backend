package com.yedu.backend.global.config.security.jwt.usecase;

import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.global.config.security.jwt.constant.Role;
import com.yedu.backend.global.config.security.jwt.util.JwtUtils;
import jakarta.servlet.http.Cookie;
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
    private static final String ACCESS_COOKIE = "accessToken";
    private static final String REFRESH_COOKIE = "refreshToken";
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    public void signIn(Admin admin, HttpServletResponse response) {
        generateAdminToken(admin, response);
    }

    public void logout(Admin admin, HttpServletResponse response) {
        deleteCookie(response);
        jwtUtils.makeExpired(admin.getAdminId());
    }

    public void regenerateToken(Admin admin, HttpServletRequest request, HttpServletResponse response) {
        log.info("regenerate 진입");
        jwtUtils.checkRedis(admin.getAdminId(), request);
        generateAdminToken(admin, response);
    }


    private void generateAdminToken(Admin admin, HttpServletResponse response) {
        String accessToken = jwtUtils.generateAccessToken(admin.getAdminId(), Role.ADMIN);
        String refreshToken = jwtUtils.generateRefreshToken(admin.getAdminId(), Role.ADMIN);
        accessTokenCookie(response, accessToken);
        refreshTokenCookie(response, refreshToken);
    }

    private void accessTokenCookie(HttpServletResponse response, String accessToken) {
        Cookie accessCookie = new Cookie(ACCESS_COOKIE, accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(accessExpiration);
        response.addCookie(accessCookie);
    }

    private void refreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie(REFRESH_COOKIE, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(refreshExpiration);
        response.addCookie(refreshCookie);
    }

    private void deleteCookie(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie(REFRESH_COOKIE, null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
        Cookie accessCookie = new Cookie(ACCESS_COOKIE, null);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);
    }
}