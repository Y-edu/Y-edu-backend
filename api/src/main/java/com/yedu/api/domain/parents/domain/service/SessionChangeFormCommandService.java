package com.yedu.api.domain.parents.domain.service;

import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import com.yedu.api.domain.parents.application.dto.req.AcceptChangeSessionRequest;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.repository.SessionChangeFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class SessionChangeFormCommandService {

  private final SessionChangeFormRepository sessionChangeFormRepository;
  private final ClassSessionRepository classSessionRepository;

  public void save(Parents parents, AcceptChangeSessionRequest request) {
    ClassSession session = classSessionRepository.findById(request.sessionId()).orElseThrow();

    SessionChangeForm sessionChangeForm = SessionChangeForm.builder()
        .lastSessionBeforeChange(session)
        .parents(parents)
        .changeType(request.type())
        .build();

    sessionChangeFormRepository.save(sessionChangeForm);
  }
}
