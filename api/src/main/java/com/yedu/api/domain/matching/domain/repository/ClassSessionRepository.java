package com.yedu.api.domain.matching.domain.repository;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.constant.PayStatus;
import io.lettuce.core.dynamic.annotation.Param;
import io.micrometer.common.KeyValues;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassSessionRepository
    extends JpaRepository<ClassSession, Long>, CustomClassSessionRepository {

  List<ClassSession> findBySessionDateAndCancelIsFalseAndCompletedIsFalse(LocalDate sessionDate);

  List<ClassSession> findByClassManagementAndSessionDateIsGreaterThanEqual(
      ClassManagement classManagement, LocalDate date);

  List<ClassSession> findByClassManagementAndSessionDateBetween(
      ClassManagement classManagement, LocalDate startDate, LocalDate endDate);

  void deleteByClassManagementAndCancelIsFalseAndCompletedIsFalseAndSessionDateIsGreaterThanEqual(
          ClassManagement classManagement, LocalDate changeStartDate);

  boolean existsClassSessionByClassManagement(ClassManagement classManagement);

  Optional<ClassSession> findFirstByClassManagementAndSessionDateBeforeOrderBySessionDateDesc(
          ClassManagement classManagement, LocalDate sessionDate);

  @Query(
      """
    select sum(cs.realClassTime) from ClassSession cs
    where cs.classManagement.classMatching.classMatchingId = :matchingId
      and cs.cancel = false
      and cs.completed = true
      and cs.sessionDate between :startDate and :endDate
  """)
  Integer sumClassTime(
      @Param("matchingId") Long matchingId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query(
      """
    select sum(cs.realClassTime) from ClassSession cs
    where cs.classManagement.classMatching.classMatchingId = :matchingId
      and cs.completed = true
  """)
  Integer sumTotalClassTime(
      @Param("cancelReason") String cancelReason, @Param("matchingId") Long matchingId);

  @Query(
      """
  select cs
  from ClassSession cs
  where cs.classManagement in :classManagements
    and cs.cancel = false
    and cs.isTodayCancel = false
    and cs.completed = false
    and cs.sessionDate between :startDate and :endDate
""")
  List<ClassSession> findSession(
      @Param("classManagements") List<ClassManagement> classManagements,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  List<ClassSession> findAllByRemindIsTrueAndCompletedIsFalseAndCancelIsFalse();

  List<ClassSession> findAllByClassManagementAndCompletedIsTrueAndPayStatusIn(ClassManagement classManagement, List<PayStatus> payStatus);

  List<ClassSession> findAllByClassManagement(ClassManagement management);
}
