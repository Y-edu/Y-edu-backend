package com.yedu.api.domain.parents.domain.repository;

import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, String> {
  void deleteAllByParents_PhoneNumber(String phoneNumber);
}
