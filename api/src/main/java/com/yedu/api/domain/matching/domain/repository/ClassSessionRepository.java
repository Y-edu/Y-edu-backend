package com.yedu.api.domain.matching.domain.repository;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  Optional<ClassSession> findFirstByClassManagementAndSessionDateBeforeAndCompletedTrueAndCancelFalseAndRoundIsNotNullOrderBySessionDateDesc(
      ClassManagement classManagement, LocalDate sessionDate);

  @Query("""
    select sum(cs.realClassTime) from ClassSession cs 
    where cs.classManagement.classMatching.classMatchingId = :matchingId 
      and cs.cancel is false 
      and cs.completed is true 
      and cs.isTodayCancel is false
      and cs.sessionDate between :startDate and :endDate
  """)
  Integer sumClassTime(@Param("matchingId") Long matchingId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query("""
    select cs from ClassSession cs
    where cs.classManagement.classManagementId = (
      select cs2.classManagement.classManagementId
      from ClassSession cs2
      where cs2.classSessionId = :sessionId
    )
    order by cs.sessionDate asc
  """)
  List<ClassSession> findBySameClassManagementId(@Param("sessionId") Long sessionId);

  @Query("""
    UPDATE ClassSession cs
    SET cs.teacherRound = :teacherRound
    WHERE cs.classSessionId = :sessionId
  """)
  @Modifying
  void updateRoundBySessionId(@Param("sessionId") Long sessionId, @Param("teacherRound") Integer teacherRound);

  @Query("""
    SELECT COALESCE(MAX(cs.teacherRound), 0)
    FROM ClassSession cs
    WHERE cs.classManagement.classMatching.classMatchingId = :matchingId
      AND cs.teacherRound IS NOT NULL
  """)
  Integer findMaxRoundByMatchingId(@Param("matchingId") Long matchingId);
}
