package com.yedu.backend.global.config.security.jwt.filter;

import static com.yedu.backend.global.config.security.jwt.constant.Type.ACCESS;
import static com.yedu.backend.global.config.security.jwt.constant.Type.REFRESH;

import com.yedu.backend.global.config.security.jwt.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtils jwtProvider;
  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = resolveToken(request);
    if (token != null) {
      log.info("토큰 함께 요청 : {}", token);
      try {
        if (request.getRequestURI().contains("/regenerate")) {
          log.info("재발급 진행");
          jwtProvider.validateToken(token, REFRESH);
        } else {
          log.info("일반 접근");
          jwtProvider.validateToken(token, ACCESS);
        }
        Authentication authentication = jwtProvider.getAuthentication(response, token);
        log.info("authentication : {}", authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("context 인증 정보 저장 : {}", authentication.getName());
      } catch (IllegalArgumentException ex) {
        // 401 Unauthorized로 응답
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Invalid or expired token");
        return; // 필터 체인 종료, 더 이상 진행되지 않도록
      } catch (Exception ex) {
        // 500 Internal Server Error로 응답
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.getWriter().write("An error occurred during token validation");
        return; // 필터 체인 종료, 더 이상 진행되지 않도록
      }
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
      return bearerToken.split(" ")[1];
    }
    return null;
  }
}
