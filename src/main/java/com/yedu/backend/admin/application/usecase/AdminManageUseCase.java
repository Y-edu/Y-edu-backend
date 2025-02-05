package com.yedu.backend.admin.application.usecase;

import com.yedu.backend.admin.application.dto.req.LoginRequest;
import com.yedu.backend.admin.application.dto.req.ParentsKakaoNameRequest;
import com.yedu.backend.admin.application.dto.req.TeacherIssueRequest;
import com.yedu.backend.admin.domain.entity.Admin;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.admin.domain.service.AdminUpdateService;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.global.config.security.jwt.usecase.JwtUseCase;
import com.yedu.backend.global.config.security.util.EncryptorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminManageUseCase {
    private final AdminGetService adminGetService;
    private final AdminUpdateService adminUpdateService;
    private final JwtUseCase jwtUseCase;
    private final EncryptorUtils encryptorUtils;

    public void updateParentsKakaoName(long parentsId, ParentsKakaoNameRequest request) {
        Parents parents = adminGetService.parentsById(parentsId);
        adminUpdateService.updateKakaoName(parents, request.kakaoName());
    }

    public void updateProceedStatus(String applicationFormId) {
        ApplicationForm applicationForm = adminGetService.applicationFormById(applicationFormId);
        adminUpdateService.updateProceedStatus(applicationForm);
    }

    public void updateTeacherIssue(long teacherId, TeacherIssueRequest request) {
        Teacher teacher = adminGetService.teacherById(teacherId);
        adminUpdateService.updateTeacherIssue(teacher, request.issue());
    }

    public void loginAdmin(LoginRequest request, HttpServletResponse response) {
        Admin admin = adminGetService.adminByLoginId(request.id());
        if (!encryptorUtils.checkBCryptData(request.password(), admin.getPassword()))
            throw new IllegalArgumentException();
        jwtUseCase.signIn(admin, response);
    }

    public void logout(Admin admin, HttpServletResponse response) {
        if (admin == null)
            throw new IllegalArgumentException();
        jwtUseCase.logout(admin, response);
    }

    public void regenerate(Admin admin, HttpServletRequest request, HttpServletResponse response) {
        log.info("AdminManageUsecase");
        jwtUseCase.regenerateToken(admin, request, response);
    }
}
