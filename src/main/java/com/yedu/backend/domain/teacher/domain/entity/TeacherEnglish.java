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
public class TeacherEnglish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long englishId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String appealPoint;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String teachingExperience;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String teachingStyle;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String managementStyle;
    @Column(columnDefinition = "TEXT")
    private String foreignExperience;
}
