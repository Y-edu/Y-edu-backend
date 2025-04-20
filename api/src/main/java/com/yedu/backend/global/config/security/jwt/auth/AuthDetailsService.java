package com.yedu.backend.global.config.security.jwt.auth;

import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.admin.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthDetailsService implements UserDetailsService {
  private final AdminRepository adminRepository;

  @Override
  public AuthDetails loadUserByUsername(String id) {
    Admin admin = adminRepository.findById(Long.parseLong(id)).orElseThrow();
    return new AuthDetails(admin);
  }
}
