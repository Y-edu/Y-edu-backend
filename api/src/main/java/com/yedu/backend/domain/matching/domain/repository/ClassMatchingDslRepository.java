package com.yedu.backend.domain.matching.domain.repository;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import java.util.List;

public interface ClassMatchingDslRepository {
  List<ClassMatching> allByApplicationForm(String applicationFormId);
}
