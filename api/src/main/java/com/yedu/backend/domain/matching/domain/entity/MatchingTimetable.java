package com.yedu.backend.domain.matching.domain.entity;

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
public class MatchingTimetable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long timetableId;

    @Column(nullable = false)
    private LocalTime timetableTime;

    @Enumerated(EnumType.STRING)
    private Day day;

    @OneToOne(fetch = FetchType.LAZY)
    private ClassMatching classMatching;
}
