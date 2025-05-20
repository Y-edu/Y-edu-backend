package com.yedu.api;

import static com.yedu.api.global.event.mapper.BizppurioEventMapper.mapToTeacherScheduleEvent;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.cache.support.storage.ClassManagementKeyStorage;
import com.yedu.common.event.bizppurio.TeacherScheduleEvent;
import jakarta.annotation.PostConstruct;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class ApiApplicationTests {

  @Autowired
  private ClassManagementRepository repository;

  @Autowired
  private ClassManagementKeyStorage classManagementKeyStorage;

  void foo(){
  }
}
