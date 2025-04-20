package com.yedu.backend.admin.domain.repository;

import com.yedu.backend.admin.domain.entity.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
  Optional<Admin> findByLoginIdAndPassword(String id, String password);

  Optional<Admin> findByLoginId(String id);
}
