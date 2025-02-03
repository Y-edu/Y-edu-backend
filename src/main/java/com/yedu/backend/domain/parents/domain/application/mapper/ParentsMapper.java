package com.yedu.backend.domain.parents.domain.application.mapper;

import com.yedu.backend.domain.parents.domain.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;

public class ParentsMapper {
    public static Parents mapToParents(ApplicationFormRequest request) {
        return Parents.builder()
                .district(District.fromString(request.district()))
                .dong(request.dong())
                .phoneNumber(request.phoneNumber())
                .build();
    }

    public static ApplicationForm mapToApplicationForm(Parents parents, ApplicationFormRequest request) {
        char count = (char) (parents.getCount() + 96);
        return ApplicationForm.builder()
                .applicationFormId(parents.getDistrict() + String.valueOf(parents.getParentsId()) + count)
                .parents(parents)
                .age(request.age())
                .online(request.online())
                .wantedSubject(request.wantedSubject())
                .favoriteStyle(request.favoriteStyle())
                .favoriteGender(request.favoriteGender())
                .wantTime(request.wantTime())
                .classCount(request.classCount())
                .classTime(request.classTime())
                .source(request.source())
                .build();
    }
}
