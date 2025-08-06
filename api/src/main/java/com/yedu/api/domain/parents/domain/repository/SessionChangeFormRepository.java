package com.yedu.api.domain.parents.domain.repository;

import com.yedu.api.domain.parents.domain.entity.SessionChangeForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionChangeFormRepository extends JpaRepository<SessionChangeForm, Long> {

}
