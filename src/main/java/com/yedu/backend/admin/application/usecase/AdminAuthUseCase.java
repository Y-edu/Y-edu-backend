package com.yedu.backend.admin.application.usecase;

import com.yedu.backend.admin.application.dto.req.LoginRequest;
import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.global.config.security.jwt.dto.JwtResponse;
import com.yedu.backend.global.config.security.jwt.usecase.JwtUseCase;
import com.yedu.backend.global.config.security.util.EncryptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthUseCase {
    private final JwtUseCase jwtUseCase;
    private final EncryptorUtils encryptorUtils;
    private final AdminGetService adminGetService;

    public JwtResponse loginAdmin(LoginRequest request, HttpServletResponse response) {
        Admin admin = adminGetService.adminByLoginId(request.id());
        if (!encryptorUtils.checkBCryptData(request.password(), admin.getPassword()))
            throw new IllegalArgumentException();
        return jwtUseCase.signIn(admin, response);
    }

    public void logout(Admin admin) {
        if (admin == null)
            throw new IllegalArgumentException();
        jwtUseCase.logout(admin);
    }

    public JwtResponse regenerate(Admin admin, HttpServletRequest request, HttpServletResponse response) {
        return jwtUseCase.regenerateToken(admin, request, response);
    }
}
