package com.yedu.backend.domain.matching.domain.repository;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;

import java.util.List;

public interface ClassMatchingDslRepository {
    List<ClassMatching> allByApplicationForm(ApplicationForm applicationForm);
}
