package com.yedu.api.domain.parents.domain.repository;

import com.yedu.api.domain.parents.domain.entity.Parents;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentsRepository extends JpaRepository<Parents, Long> {
  Optional<Parents> findByPhoneNumber(String phoneNumber);

  void deleteAllByPhoneNumber(String phoneNumber);
}
