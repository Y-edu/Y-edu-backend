package com.yedu.backend.domain.matching.domain.entity;

import com.yedu.backend.domain.matching.domain.vo.ClassTime;
import com.yedu.backend.global.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassManagement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classManagementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_matching_id")
    private ClassMatching classMatching;

    private String textbook;

    private LocalDate firstDay;

    @Embedded
    private ClassTime classTime;


}
