package com.yedu.api.domain.parents.domain.repository;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionChangeFormRepository extends JpaRepository<SessionChangeForm, Long> {

  List<SessionChangeForm> findByLastSessionBeforeChangeIn(
      Collection<ClassSession> lastSessionBeforeChanges);

  List<SessionChangeForm> findByLastSessionBeforeChange_ClassManagementAndChangeType(
      ClassManagement lastSessionBeforeChangeClassManagement, SessionChangeType changeType);

  Optional<SessionChangeForm> findFirstByParents_PhoneNumberAndChangeType(
      String parentsPhoneNumber, SessionChangeType changeType);
}
