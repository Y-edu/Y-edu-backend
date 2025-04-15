package com.yedu.backend.global.config.security.jwt.util;

import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.global.config.security.jwt.auth.AuthDetails;
import com.yedu.backend.global.config.security.jwt.auth.AuthDetailsService;
import com.yedu.backend.global.config.security.jwt.constant.Role;
import com.yedu.backend.global.config.security.jwt.constant.Type;
import com.yedu.backend.global.exception.ApplicationException;
import com.yedu.common.redis.RedisRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.yedu.backend.global.config.security.jwt.constant.Role.*;
import static com.yedu.backend.global.config.security.jwt.constant.Type.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
    private final AuthDetailsService authDetailsService;
    private final RedisRepository redisRepository;

    @Value("${jwt.secret-key}")
    private String secret;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    private static final String ROLE = "role";
    private static final String TYPE = "type";
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    public String generateAccessToken(Long id, Role role) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        Instant accessDate = LocalDateTime.now().plusSeconds(accessExpiration).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .claim(ROLE, role)
                .claim(TYPE, ACCESS)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(accessDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long id, Role role) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        Instant refreshDate = LocalDateTime.now().plusSeconds(refreshExpiration).atZone(ZoneId.systemDefault()).toInstant();
        String refreshToken = Jwts.builder()
                .claim(ROLE, role)
                .claim(TYPE, REFRESH)
                .setSubject(String.valueOf(id))
                .setExpiration(Date.from(refreshDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        redisRepository.setValues(REFRESH.toString() + id + role, refreshToken, Duration.ofSeconds(refreshExpiration));
        return refreshToken;
    }

    public String checkRedis(Long id, HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTHORIZATION).split(" ")[1];
        Claims claims = parseClaims(refreshToken);
        String redisToken = redisRepository.getValues(REFRESH.toString() + id + claims.get(ROLE))
                .orElseThrow();
        if (!redisToken.equals(refreshToken))
            throw new IllegalArgumentException();
        return claims.get(ROLE).toString();
    }

    public void makeExpired(Long id) {
        redisRepository.deleteValues(REFRESH.toString() + id + ADMIN);
    }

    public Authentication getAuthentication(HttpServletResponse response, String token) {
        Claims claims = parseClaims(token); //여기까지 진행 확인
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get(ROLE).toString()));
        Admin admin = getDetails(response, claims).getAdmin();
        return new UsernamePasswordAuthenticationToken(admin, null, authorities);
    }

    private AuthDetails getDetails(HttpServletResponse response, Claims claims) {
        try {
            return authDetailsService.loadUserByUsername(claims.getSubject());
        } catch (ApplicationException ex) {
            jwtExceptionHandler(BAD_REQUEST, response, ex);
            throw ex;
        }
    }

    public boolean validateToken(String token, Type type) {
        try {
            Claims claims = parseClaims(token);
            if (!claims.get(TYPE).equals(type.name())) {
                log.error("토큰 검증 실패");
                throw new IllegalArgumentException();
            }
            log.info("토큰 검증 통과");
            return true;
        } catch (ExpiredJwtException e) {
            log.error("토큰 검증 실패");
            throw new IllegalArgumentException();
        } catch (Exception e) {
            log.error("토큰 검증 실패");
            throw new IllegalArgumentException();
        }
    }

    private Claims parseClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private void jwtExceptionHandler(HttpStatus status, HttpServletResponse response, ApplicationException ex) {
        response.setStatus(status.value());
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        log.error("errorCode {}, errorMessage {}", ex.getCode(), ex.getMessage());
        try {
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

