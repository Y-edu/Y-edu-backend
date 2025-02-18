package com.yedu.backend.domain.parents.domain.repository;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, String> {
    void deleteAllByParents_PhoneNumber(String phoneNumber);
}
