package com.yedu.backend.global.config.security;

import com.yedu.backend.global.config.security.jwt.constant.Role;
import com.yedu.backend.global.config.security.jwt.filter.CustomAccessDeniedHandler;
import com.yedu.backend.global.config.security.jwt.filter.CustomAuthenticationEntryPoint;
import com.yedu.backend.global.config.security.jwt.filter.JwtFilter;
import com.yedu.backend.global.config.security.jwt.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private static final String[] PASS = {"/resource/**", "/css/**", "/js/**", "/img/**", "/lib/**"};

  @Value("${aesBytesEncryptor.secret}")
  private String secretKey;

  @Value("${aesBytesEncryptor.salt}")
  private String salt;

  private final JwtUtils jwtProvider;
  private final CustomAccessDeniedHandler accessDeniedHandler;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  @Bean
  public AesBytesEncryptor aesBytesEncryptor() {
    return new AesBytesEncryptor(secretKey, salt);
  }

  @Bean
  protected SecurityFilterChain config(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(corsConfigurer -> corsConfigurer.configurationSource(source()))
        .httpBasic(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    http.formLogin(AbstractHttpConfigurer::disable).logout(logout -> logout.disable());
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/admin/login")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasAuthority(Role.ADMIN.name())
                    .anyRequest()
                    .permitAll())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            exceptions ->
                exceptions
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource source() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addExposedHeader("Authorization");
    configuration.addExposedHeader("RefreshToken");
    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
