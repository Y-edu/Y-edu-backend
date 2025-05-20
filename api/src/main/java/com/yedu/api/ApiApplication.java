package com.yedu.api;

import static com.yedu.api.global.event.mapper.BizppurioEventMapper.mapToTeacherScheduleEvent;

import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.common.event.bizppurio.TeacherScheduleEvent;
import jakarta.annotation.PostConstruct;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;


@SpringBootApplication(scanBasePackages = "com.yedu")
@EnableScheduling
@EnableAsync
public class ApiApplication {

  @Autowired
  private ClassManagementRepository managementRepository;

  @Autowired
  ApplicationEventPublisher publisher;

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }


}
