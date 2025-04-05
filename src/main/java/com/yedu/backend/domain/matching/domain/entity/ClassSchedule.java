package com.yedu.backend.domain.matching.domain.entity;

import com.yedu.backend.domain.matching.domain.vo.ClassTime;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_management_id")
    private ClassManagement classManagement;

    @Enumerated(EnumType.STRING)
    private Day day;

    @Embedded
    private ClassTime classTime;
}
