package com.yedu.backend.domain.teacher.domain.entity;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import com.yedu.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherAvailable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long availableId;
    @Column(nullable = false)
    private LocalTime availableTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Day day;
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;
}
