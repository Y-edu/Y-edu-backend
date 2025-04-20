package com.yedu.backend.domain.parents.domain.repository;

import com.yedu.backend.domain.parents.domain.entity.ApplicationFormAvailable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationFormAvailableRepository
    extends JpaRepository<ApplicationFormAvailable, Long> {

  List<ApplicationFormAvailable> findAllByApplicationFormId(String applicationFormId);
}
