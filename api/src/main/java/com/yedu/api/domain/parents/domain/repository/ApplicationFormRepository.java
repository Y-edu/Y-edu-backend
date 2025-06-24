package com.yedu.api.domain.parents.domain.repository;

import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.teacher.domain.entity.constant.District;
import com.yedu.common.type.ClassType;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, String> {
  void deleteAllByParents_PhoneNumber(String phoneNumber);

  Optional<ApplicationForm> findByParentsAndDistrictAndWantedSubjectAndAgeAndClassCountAndCreatedAtAfter(Parents parents, District district, ClassType wantedSubject,
      String age, String classCount, LocalDateTime before);
}
