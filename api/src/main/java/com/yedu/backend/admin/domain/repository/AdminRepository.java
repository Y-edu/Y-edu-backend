package com.yedu.backend.admin.domain.repository;

import com.yedu.backend.admin.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByLoginIdAndPassword(String id, String password);
    Optional<Admin> findByLoginId(String id);
}
