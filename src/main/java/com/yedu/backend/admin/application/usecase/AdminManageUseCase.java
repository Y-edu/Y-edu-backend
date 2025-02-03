package com.yedu.backend.admin.application.usecase;

import com.yedu.backend.admin.application.dto.req.ParentsKakaoNameRequest;
import com.yedu.backend.admin.domain.service.AdminGetService;
import com.yedu.backend.admin.domain.service.AdminUpdateService;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class AdminManageUseCase {
    private final AdminGetService adminGetService;
    private final AdminUpdateService adminUpdateService;

    public void updateParentsKakaoName(long parentsId, ParentsKakaoNameRequest request) {
        Parents parents = adminGetService.parentsById(parentsId);
        adminUpdateService.updateKakaoName(parents, request.kakaoName());
    }
}
