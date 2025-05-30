package com.yedu.api.admin.domain.repository;

import com.yedu.api.admin.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
