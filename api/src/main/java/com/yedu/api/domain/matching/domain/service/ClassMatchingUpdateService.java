package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.req.ClassMatchingRefuseRequest;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import org.springframework.stereotype.Service;

@Service
public class ClassMatchingUpdateService {
  public void updateRefuse(ClassMatching classMatching, ClassMatchingRefuseRequest request) {
    classMatching.updateRefuse(request.refuseReason());
  }

  public void updateAccept(ClassMatching classMatching) {
    classMatching.updateAccept();
  }
}
