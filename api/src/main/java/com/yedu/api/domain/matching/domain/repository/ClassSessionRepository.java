package com.yedu.api.domain.matching.domain.repository;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {

  List<ClassSession> findBySessionDateAndCancelIsFalseAndCompletedIsFalse(LocalDate sessionDate);

  List<ClassSession> findByClassManagementAndSessionDateIsGreaterThanEqual(
      ClassManagement classManagement, LocalDate date);

  List<ClassSession> findByClassManagementAndSessionDateBetween(
      ClassManagement classManagement, LocalDate startDate, LocalDate endDate);

  void deleteByClassManagementAndCancelIsFalseAndCompletedIsFalse(ClassManagement classManagement);

  boolean existsClassSessionByClassManagement(ClassManagement classManagement);
}
