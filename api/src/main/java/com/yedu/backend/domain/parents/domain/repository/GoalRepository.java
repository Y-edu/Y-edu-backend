
package com.yedu.backend.domain.parents.domain.repository;

import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByApplicationForm(ApplicationForm applicationForm);

    void deleteAllByApplicationForm_Parents_PhoneNumber(String phoneNumber);
}
