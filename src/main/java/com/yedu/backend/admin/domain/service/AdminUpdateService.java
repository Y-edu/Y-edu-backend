package com.yedu.backend.admin.domain.service;

import com.yedu.backend.domain.parents.domain.entity.Parents;
import org.springframework.stereotype.Service;

@Service
public class AdminUpdateService {
    public void updateKakaoName(Parents parents, String kakaoName) {
        parents.updateKakaoName(kakaoName);
    }
}
