package com.yedu.backend.domain.parents.domain.entity;

import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
public class ApplicationForm extends BaseEntity {
    @Id
    private long applicationFormId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Parents parents;
    private int age; // 아이나이
    private boolean online;
    private String district; // todo : enum으로 변경
    private String dong; // todo : enum으로 변경
    private ClassType wantedSubject;
    private boolean acceptStatus; // 수락 상태
    private boolean proceedStatus; // 처리 상태
}
