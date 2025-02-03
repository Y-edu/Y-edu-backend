
package com.yedu.backend.domain.parents.domain.repository;

import com.yedu.backend.domain.parents.domain.entity.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentsRepository extends JpaRepository<Parents, Long> {
    Optional<Parents> findByPhoneNumber(String phoneNumber);
}
