package com.yedu.backend.domain.teacher.domain.entity;

import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class TeacherInfo {
    //기본정보
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String nickName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column
    private String birth;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeacherGender gender; //T : 남 / F : 여

    private String profile; //프로필 사진
    private String video; // 과외 영상

    public void updateProfile(String profile) {
        this.profile = profile;
        //todo : 나중에 과외 영상 추가 필요
    }

    public void updateVideo(String video) {
        this.video = video;
    }
}
