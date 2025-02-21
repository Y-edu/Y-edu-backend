package com.yedu.backend.domain.teacher.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherMath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mathId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;
    @Column(columnDefinition = "TEXT")
    private String appealPoint;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String teachingExperience;
    @Column(nullable = false)
    private int teachingHistory;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String teachingStyle;
    @Column(columnDefinition = "TEXT")
    private String managementStyle;
}
