package com.yedu.api.domain.matching.domain.repository;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomClassSessionRepository {

  Page<ClassSession> findByClassManagementAndSessionDateBetween(
      ClassManagement classManagement, LocalDate startDate, LocalDate endDate, Pageable pageable);

  Page<ClassSession> findByClassManagementAndSessionDateBetweenAndCompleted(ClassManagement classManagement, LocalDate startDate, LocalDate endDate, boolean completed, Pageable pageable);


}
