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
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassSessionQueryService {

  private final ClassManagementQueryService classManagementQueryService;

  private final ClassSessionRepository classSessionRepository;

  public SessionResponse query(List<ClassMatching> classMatchings) {
    LocalDate now = LocalDate.now();
    Map<String, List<Schedule>> scheduleMap =
        classMatchings.stream()
            .map(
                matching -> {
                  Optional<ClassManagement> optionalManagement =
                      classManagementQueryService.query(matching.getClassMatchingId());
                  return optionalManagement.map(
                      cm ->
                          Map.entry(
                              matching.getApplicationForm().getApplicationFormId(),
                              SessionResponse.from(
                                  classSessionRepository.findByClassManagementAndSessionDateBetween(
                                      cm,
                                      now.with(TemporalAdjusters.firstDayOfMonth()),
                                      now.with(TemporalAdjusters.lastDayOfMonth())))));
                })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry ->
                        entry.getValue().stream()
                            .sorted(
                                Comparator.comparing(SessionResponse.Schedule::classDate)
                                    .thenComparing(SessionResponse.Schedule::classStart))
                            .toList()));

    return new SessionResponse(scheduleMap);
  }

  public RetrieveSessionDateResponse querySessionDate(Long sessionId) {
    return classSessionRepository
        .findById(sessionId)
        .map(it-> new RetrieveSessionDateResponse(it.getSessionDate(), it.isCompleted(), it.getClassManagement().getClassMatching().getTeacher().getTeacherId()))
        .orElseThrow();
  }

  public List<ClassSession> query(ClassManagement management) {
    LocalDate now = LocalDate.now();
    return classSessionRepository.findByClassManagementAndSessionDateBetween(
        management,
        now.with(TemporalAdjusters.firstDayOfMonth()),
        now.with(TemporalAdjusters.lastDayOfMonth()));
  }
}
