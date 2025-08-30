package com.yedu.api.domain.matching.domain.repository;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassMatchingRepository
    extends JpaRepository<ClassMatching, Long>, ClassMatchingDslRepository {

  @Query(
      """
    SELECT cm FROM ClassSession cs
    JOIN cs.classManagement cg
    JOIN cg.classMatching cm
    WHERE cs.classSessionId = :sessionId
""")
  Optional<ClassMatching> findBySessionId(@Param("sessionId") Long sessionId);

  Optional<ClassMatching>
      findByApplicationForm_ApplicationFormIdAndTeacher_TeacherIdAndTeacher_TeacherInfo_PhoneNumber(
          String applicationFormId, long teacherId, String phoneNumber);

  List<ClassMatching> findByApplicationForm(ApplicationForm applicationForm);

  List<ClassMatching> findByTeacherAndMatchStatus(Teacher teacher, MatchingStatus matchStatus);

  List<ClassMatching> findByMatchStatusIn(List<MatchingStatus> matchStatus, Sort sort);

  List<ClassMatching> findByClassMatchingIdInAndMatchStatusIn(
      List<Long> classMatchingIds, List<MatchingStatus> matchStatus, Sort sort);

  void deleteAllByApplicationForm_Parents_PhoneNumber(String phoneNumber);

  void deleteAllByTeacher_TeacherInfo_PhoneNumber(String phoneNumber);

  List<ClassMatching> findByTeacherAndMatchStatusIn(
      Teacher teacher, Collection<MatchingStatus> matchStatuses);

  @Query(
      """
    select cm from ClassMatching cm
      where cm.matchStatus = '최종매칭'
        and cm.applicationForm.parents = :parents
  """)
  List<ClassMatching> findByParent(Parents parents);
}
