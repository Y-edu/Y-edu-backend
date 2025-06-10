package com.yedu.api.domain.matching.domain.service;

import com.yedu.api.domain.matching.application.dto.res.RetrieveSessionDateResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse;
import com.yedu.api.domain.matching.application.dto.res.SessionResponse.Schedule;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.repository.ClassSessionRepository;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassSessionQueryService {

  private final ClassManagementQueryService classManagementQueryService;

  private final ClassSessionRepository classSessionRepository;

  public SessionResponse query(List<ClassMatching> classMatchings, Boolean isComplete, Pageable pageable) {
    LocalDate now = LocalDate.now();
    LocalDate startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
    LocalDate endOfMonth = now.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

    Map<String, Page<SessionResponse.Schedule>> scheduleMap = classMatchings.stream()
        .map(matching -> {
          String applicationFormId = matching.getApplicationForm().getApplicationFormId();
          Optional<ClassManagement> optionalManagement =
              classManagementQueryService.query(matching.getClassMatchingId());

          if (optionalManagement.isEmpty()) {
            return null;
          }

          ClassManagement cm = optionalManagement.get();

          Page<ClassSession> sessions = Optional.ofNullable(isComplete).map(
                  it -> classSessionRepository.findByClassManagementAndSessionDateBetweenAndCompleted(
                      cm, startOfMonth, endOfMonth, isComplete, pageable))
              .orElseGet(() -> classSessionRepository
                  .findByClassManagementAndSessionDateBetween(cm, startOfMonth, endOfMonth,
                      pageable));

          // Page<ClassSession> → Page<Schedule>
          Page<SessionResponse.Schedule> schedulePage = SessionResponse.from(sessions);

          return Map.entry(applicationFormId, schedulePage);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return new SessionResponse(scheduleMap);
  }

  public RetrieveSessionDateResponse querySessionDate(Long sessionId) {
    return classSessionRepository
        .findById(sessionId)
        .map(
            it ->
                new RetrieveSessionDateResponse(
                    it.getSessionDate(),
                    it.isCompleted(),
                    it.getClassManagement().getClassMatching().getTeacher().getTeacherId(),
                    it.getClassTime()))
        .orElseThrow();
  }

  public List<ClassSession> query(ClassManagement management) {
    LocalDate now = LocalDate.now();
    return classSessionRepository.findByClassManagementAndSessionDateBetween(
        management,
        now.with(TemporalAdjusters.firstDayOfMonth()),
        now.with(TemporalAdjusters.lastDayOfMonth())
        );
  }
}
