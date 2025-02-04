package com.yedu.backend.domain.matching.domain.repository;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassMatchingRepository extends JpaRepository<ClassMatching, Long> {
    List<ClassMatching> findAllByApplicationForm(ApplicationForm applicationForm);
}
